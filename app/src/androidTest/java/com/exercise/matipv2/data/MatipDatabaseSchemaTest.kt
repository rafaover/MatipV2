package com.exercise.matipv2.data

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.exercise.matipv2.data.local.MatipDatabase
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Baseline for schema version 7, the version shipped to users. No migrations exist yet.
 * When a new version is added, add a test here that starts at the previous version, runs the
 * migration and checks the data survived.
 */
@RunWith(AndroidJUnit4::class)
class MatipDatabaseSchemaTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    @get:Rule
    val helper = MigrationTestHelper(instrumentation, MatipDatabase::class.java)

    @Test
    fun version7Database_opensWithCurrentSchemaAndKeepsData() {
        helper.createDatabase(TEST_DATABASE_NAME, SHIPPED_VERSION).apply {
            execSQL(
                "INSERT INTO lists (id, list_name, user_id, cloud_id) " +
                    "VALUES (1, 'Dinner', 'user-1', 'cloud-list-1')"
            )
            execSQL(
                "INSERT INTO tips (id, tip_amount, tip_percent, list_id, date_created, user_id, cloud_id) " +
                    "VALUES (1, '\$2.50', '15', 1, '26/11/2026', 'user-1', 'cloud-tip-1')"
            )
            close()
        }

        // No destructive fallback here: a schema mismatch must fail the test, not wipe the data.
        val database = Room
            .databaseBuilder(instrumentation.targetContext, MatipDatabase::class.java, TEST_DATABASE_NAME)
            .build()
        helper.closeWhenFinished(database)

        val listName = database.openHelper.readableDatabase
            .query("SELECT list_name FROM lists WHERE id = 1")
            .use { cursor -> cursor.moveToFirst(); cursor.getString(0) }
        val tipAmount = database.openHelper.readableDatabase
            .query("SELECT tip_amount FROM tips WHERE id = 1")
            .use { cursor -> cursor.moveToFirst(); cursor.getString(0) }

        assertEquals("Dinner", listName)
        assertEquals("\$2.50", tipAmount)
    }

    private companion object {
        const val TEST_DATABASE_NAME = "matip_schema_test"
        const val SHIPPED_VERSION = 7
    }
}
