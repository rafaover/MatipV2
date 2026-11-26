package com.exercise.matipv2.data

import android.content.Context
import androidx.room.Room
import androidx.test.filters.SmallTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.exercise.matipv2.data.local.MatipDatabase
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@SmallTest
@RunWith(AndroidJUnit4::class)
class MatipDatabaseTest {

    private lateinit var db: MatipDatabase

    @Before
    @Throws(IOException::class)
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MatipDatabase::class.java)
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun listDao() {
        val listDao = db.listDao()
        assertNotNull(listDao)
    }

    @Test
    fun tipDao() {
        val tipDao = db.tipDao()
        assertNotNull(tipDao)
    }
}