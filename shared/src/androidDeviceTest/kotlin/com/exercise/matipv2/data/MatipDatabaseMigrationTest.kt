package com.exercise.matipv2.data

import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.exercise.matipv2.data.local.MATIP_MIGRATIONS
import com.exercise.matipv2.data.local.MIGRATION_2_3
import com.exercise.matipv2.data.local.MIGRATION_3_4
import com.exercise.matipv2.data.local.MIGRATION_4_5
import com.exercise.matipv2.data.local.MIGRATION_5_6
import com.exercise.matipv2.data.local.MIGRATION_6_7
import com.exercise.matipv2.data.local.MatipDatabase
import com.exercise.matipv2.di.createMatipDatabase
import com.exercise.matipv2.di.matipDatabaseBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith

/**
 * One test per migration: create the database at the old version with the exported schema,
 * run the migration, check the new shape is valid and the old rows survived.
 */
@RunWith(AndroidJUnit4::class)
class MatipDatabaseMigrationTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    private val databaseFile = context.getDatabasePath(TEST_DATABASE_NAME)
    private var migratedConnection: SQLiteConnection? = null

    @get:Rule
    val helper = MigrationTestHelper(instrumentation, databaseFile, BundledSQLiteDriver(), MatipDatabase::class)

    @Before
    fun deleteLeftoverDatabase() {
        context.deleteDatabase(TEST_DATABASE_NAME)
    }

    @After
    fun closeAndDeleteDatabase() {
        migratedConnection?.close()
        context.deleteDatabase(TEST_DATABASE_NAME)
    }

    @Test
    fun migrate2To3_turnsEventsIntoListsAndKeepsTips() {
        helper.createDatabase(version = 2).apply {
            execSQL("INSERT INTO events (id, event_name) VALUES (1, 'Dinner')")
            execSQL("INSERT INTO tips (id, tip_amount, tip_percent, event_id) VALUES (1, '\$5.00', '10', 1)")
            close()
        }

        val database = migrate(toVersion = 3, MIGRATION_2_3)

        assertEquals(listOf("1|Dinner"), database.rows("SELECT id, list_name FROM lists"))
        assertEquals(listOf("1|\$5.00|10|1"), database.rows("SELECT id, tip_amount, tip_percent, list_id FROM tips"))
    }

    @Test
    fun migrate3To4_addsTipDateAndKeepsTips() {
        helper.createDatabase(version = 3).apply {
            execSQL("INSERT INTO lists (id, list_name) VALUES (1, 'Dinner')")
            execSQL("INSERT INTO tips (id, tip_amount, tip_percent, list_id) VALUES (1, '\$5.00', '10', 1)")
            close()
        }

        val database = migrate(toVersion = 4, MIGRATION_3_4)

        assertEquals(listOf("1|\$5.00|10|1|"), database.rows("SELECT id, tip_amount, tip_percent, list_id, date_created FROM tips"))
        assertEquals(listOf("1|Dinner"), database.rows("SELECT id, list_name FROM lists"))
    }

    @Test
    fun migrate4To5_keepsEverything() {
        helper.createDatabase(version = 4).apply {
            execSQL("INSERT INTO lists (id, list_name) VALUES (1, 'Dinner')")
            execSQL("INSERT INTO tips (id, tip_amount, tip_percent, list_id, date_created) VALUES (1, '\$5.00', '10', 1, '01/01/2025')")
            close()
        }

        val database = migrate(toVersion = 5, MIGRATION_4_5)

        assertEquals(listOf("1|Dinner"), database.rows("SELECT id, list_name FROM lists"))
        assertEquals(listOf("1|01/01/2025"), database.rows("SELECT id, date_created FROM tips"))
    }

    @Test
    fun migrate5To6_addsUserIdAndKeepsRows() {
        helper.createDatabase(version = 5).apply {
            execSQL("INSERT INTO lists (id, list_name) VALUES (1, 'Dinner')")
            execSQL("INSERT INTO tips (id, tip_amount, tip_percent, list_id, date_created) VALUES (1, '\$5.00', '10', 1, '01/01/2025')")
            close()
        }

        val database = migrate(toVersion = 6, MIGRATION_5_6)

        assertEquals(listOf("1|Dinner|"), database.rows("SELECT id, list_name, user_id FROM lists"))
        assertEquals(listOf("1|\$5.00|"), database.rows("SELECT id, tip_amount, user_id FROM tips"))
    }

    @Test
    fun migrate6To7_addsCloudIdAndKeepsRows() {
        helper.createDatabase(version = 6).apply {
            execSQL("INSERT INTO lists (id, list_name, user_id) VALUES (1, 'Dinner', 'user-1')")
            execSQL("INSERT INTO tips (id, tip_amount, tip_percent, list_id, date_created, user_id) VALUES (1, '\$5.00', '10', 1, '01/01/2025', 'user-1')")
            close()
        }

        val database = migrate(toVersion = 7, MIGRATION_6_7)

        assertEquals(listOf("1|Dinner|user-1|"), database.rows("SELECT id, list_name, user_id, cloud_id FROM lists"))
        assertEquals(listOf("1|user-1|"), database.rows("SELECT id, user_id, cloud_id FROM tips"))
    }

    @Test
    fun oldestSchema_opensThroughTheProductionBuilderWithoutLosingData() = runTest {
        helper.createDatabase(version = 2).apply {
            execSQL("INSERT INTO events (id, event_name) VALUES (1, 'Dinner')")
            execSQL("INSERT INTO tips (id, tip_amount, tip_percent, event_id) VALUES (1, '\$5.00', '10', 1)")
            close()
        }

        val database = createMatipDatabase(
            matipDatabaseBuilder(instrumentation.targetContext, TEST_DATABASE_NAME)
        )
        val lists = database.listDao().getAllLists(userId = null).first()
        val tips = database.tipDao().getAllTips(userId = null).first()
        database.close()

        assertEquals(listOf("Dinner"), lists.map { it.name })
        assertEquals(listOf("\$5.00"), tips.map { it.tipAmount })
    }

    @Test
    fun versionWithoutMigrationPath_failsLoudlyAndKeepsTheData() = runTest {
        // Version 1 was never exported, so there is no way to migrate it.
        databaseFile.parentFile?.mkdirs()
        SQLiteDatabase.openOrCreateDatabase(databaseFile, null).use { legacy ->
            legacy.execSQL("CREATE TABLE tips (id INTEGER PRIMARY KEY, tip_amount TEXT)")
            legacy.execSQL("INSERT INTO tips (id, tip_amount) VALUES (1, '\$9.99')")
            legacy.version = 1
        }

        val database = createMatipDatabase(
            matipDatabaseBuilder(context, TEST_DATABASE_NAME)
        )
        assertFailsWith<IllegalStateException> { database.tipDao().getAllTips(userId = null).first() }
        database.close()

        SQLiteDatabase.openDatabase(databaseFile.path, null, SQLiteDatabase.OPEN_READONLY).use { legacy ->
            legacy.rawQuery("SELECT tip_amount FROM tips", null).use { cursor ->
                assertEquals(1, cursor.count)
                cursor.moveToFirst()
                assertEquals("\$9.99", cursor.getString(0))
            }
        }
    }

    @Test
    fun migrations_coverEveryStepFromVersion2To7() {
        val steps = MATIP_MIGRATIONS.map { it.startVersion to it.endVersion }
        assertEquals((2..6).map { it to it + 1 }, steps)
    }

    private fun migrate(toVersion: Int, migration: Migration): SQLiteConnection =
        helper.runMigrationsAndValidate(toVersion, listOf(migration)).also { migratedConnection = it }

    /** Reads every row as its columns joined with "|", with NULL shown as an empty text. */
    private fun SQLiteConnection.rows(sql: String): List<String> =
        prepare(sql).use { statement ->
            val rows = mutableListOf<String>()
            while (statement.step()) {
                rows += (0 until statement.getColumnCount()).joinToString("|") { column ->
                    if (statement.isNull(column)) "" else statement.getText(column)
                }
            }
            rows
        }

    private companion object {
        const val TEST_DATABASE_NAME = "matip_migration_test"
    }
}
