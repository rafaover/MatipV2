package com.exercise.matipv2.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.exercise.matipv2.data.local.MatipDatabase
import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.Tip
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@MediumTest
class ListDaoTests {
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

    /* Test case to insert and read list */
    @Test
    @Throws(Exception::class)
    fun insertList_GetList() = runTest {
        val list = List(id = 1, name = "ListTest")
        listDao.insertList(list)
        val byListName = listDao.getListByName("ListTest").first()
        assertEquals(byListName, list)
    }

    /* Test case to insert and delete list */
    @Test
    @Throws(Exception::class)
    fun insertList_DeleteList() = runTest {
        val list = List(id = 1, name = "ListTest")
        listDao.insertList(list)
        listDao.deleteList(list)
        val byListName = listDao.getListByName("ListTest").first()
        assertEquals(byListName, null)
    }

    /* Test case to get all Lists and Read all */
    @Test
    @Throws(Exception::class)
    fun insertLists_getAllLists() = runTest {
        for (i in 1..5) {
            val list = List(id = i, name = "ListTest$i")
            listDao.insertList(list)
        }
        val allLists = listDao.getAllLists().first()
        assertEquals(allLists.size, 5)
        assertEquals(allLists[0].name, "ListTest1")
        assertEquals(allLists[1].name, "ListTest2")
    }

    @Test
    @Throws(Exception::class)
    fun updateList_getList() = runTest {
        val list = List(id = 1, name = "ListTest")
        listDao.insertList(list)
        val updatedList = List(id = 1, name = "ListTestUpdated")
        listDao.updateList(updatedList)
        val byListName = listDao.getListByName("ListTestUpdated").first()
        assertEquals(byListName, updatedList)
    }

    @Test
    @Throws(Exception::class)
    fun getAllListsWithTips() = runBlocking {
        // Add 10 Lists
        for (i in 1..10) {
            val list = List(id = i, name = "ListTest$i")
            listDao.insertList(list)
        }
        // Add 3 tips to ListTest1
        for (i in 1..3) {
            val tip = Tip(
                id = i,
                tipAmount = "${i*250}",
                tipPercent = "${i*2}",
                listId = 1,
                dateCreated = "01/01/2021"
            )
            tipDao.insertTip(tip)
        }
        // Add 3 tips to ListTest2
        for (i in 4..6) {
            val tip = Tip(
                id = i,
                tipAmount = "${i*250}",
                tipPercent = "${i*3}",
                listId = 2,
                dateCreated = "01/01/2021"
            )
            tipDao.insertTip(tip)
        }
        val getAllLists = listDao.getAllLists().first() // 10
        val listsWithTips = listDao.getAllListsWithTips().first() // 2
        assertEquals(getAllLists.size, 10)
        assertEquals(listsWithTips.size, 2)
    }
}
