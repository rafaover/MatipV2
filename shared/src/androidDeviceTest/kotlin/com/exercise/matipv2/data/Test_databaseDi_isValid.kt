package com.exercise.matipv2.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.exercise.matipv2.data.local.MatipDatabase
import com.exercise.matipv2.di.createMatipDatabase
import com.exercise.matipv2.di.matipDatabaseBuilder
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class DatabaseDiIsValidTest {

    private lateinit var mockDatabase: MatipDatabase
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        // Mocking the database creation to return a mock database
        mockDatabase = Room
            .inMemoryDatabaseBuilder(context, MatipDatabase::class.java)
            .build()
    }

    @Test
    fun createMatipDatabaseReturnsNonNullDatabase() {
        val database = createMatipDatabase(matipDatabaseBuilder(context, name = "di_is_valid_test"))
        assertNotNull(database)
        database.close()
    }

    @Test
    fun listDaoIsNotNull() {
        val listDao = mockDatabase.listDao()
        assertNotNull(listDao)
    }

    @Test
    fun tipDaoIsNotNull() {
        val tipDao = mockDatabase.tipDao()
        assertNotNull(tipDao)
    }
}