package com.exercise.matipv2.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.exercise.matipv2.data.local.MatipDatabase
import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.Tip
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@MediumTest
class TipDaoTests {
//    @get: Rule
//    val dispatcherRule = TestDispatcherRule()

    private lateinit var db: MatipDatabase
    private lateinit var listDao: ListDao
    private lateinit var tipDao: TipDao

    @Before
    @Throws(IOException::class)
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, MatipDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        listDao = db.listDao()
        tipDao = db.tipDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertTip_getTip() = runTest {
        val tip = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 999, dateCreated = "12/07/2024")
        tipDao.insertTip(tip)
        val getTip = tipDao.getAllTips().first()[0]
        assertEquals(getTip.tipAmount, "100")
    }

    @Test
    @Throws(Exception::class)
    fun deleteTip() = runTest {
        val tip = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 999, dateCreated = "12/07/2024")
        tipDao.insertTip(tip)
        tipDao.deleteTip(tip)
        val getTip = tipDao.getAllTips().first()
        assertEquals(getTip.size, 0)
    }

    @Test
    @Throws(Exception::class)
    fun updateTip() = runTest {
        val tip = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 999, dateCreated = "12/07/2024")
        tipDao.insertTip(tip)
        val tip1 = Tip(id = 1, tipAmount = "200", tipPercent = "10", listId = 999, dateCreated = "12/07/2024")
        tipDao.updateTip(tip1)
        val updatedTip = tipDao.getAllTips().first()[0]
        assertEquals(updatedTip.tipAmount, "200")
    }

    @Test
    @Throws(Exception::class)
    fun getAllTips() = runTest {
        val tip1 = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 999, dateCreated = "12/07/2024")
        val tip2 = Tip(id = 2, tipAmount = "200", tipPercent = "20", listId = 999, dateCreated = "12/07/2024")
        tipDao.insertTip(tip1)
        tipDao.insertTip(tip2)
        val getTips = tipDao.getAllTips().first()
        assertEquals(getTips.size, 2)
    }

    @Test
    @Throws(Exception::class)
    fun getLastTipSaved() = runTest {
        val tip1 = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 999, dateCreated = "12/07/2024")
        val tip2 = Tip(id = 2, tipAmount = "200", tipPercent = "20", listId = 999, dateCreated = "12/07/2024")
        tipDao.insertTip(tip1)
        tipDao.insertTip(tip2)
        val lastTip = tipDao.getLastTipSaved()
        assertEquals(lastTip.id, 2)
    }

    @Test
    @Throws(Exception::class)
    fun getAllTipsFromEvents() = runTest {
        val tip1 = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 1, dateCreated = "12/07/2024")
        val tip2 = Tip(id = 2, tipAmount = "200", tipPercent = "20", listId = 1, dateCreated = "12/07/2024")
        val tip3 = Tip(id = 3, tipAmount = "300", tipPercent = "30", listId = 2, dateCreated = "12/07/2024")
        tipDao.insertTip(tip1)
        tipDao.insertTip(tip2)
        tipDao.insertTip(tip3)
        val getTips = tipDao.getAllTipsFromList(1).first()
        assertEquals(getTips.size, 2)
    }


}
