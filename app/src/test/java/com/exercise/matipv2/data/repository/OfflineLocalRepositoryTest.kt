package com.exercise.matipv2.data.repository

import androidx.test.filters.MediumTest
import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.ListWithTips
import com.exercise.matipv2.data.local.model.Tip
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@MediumTest
@RunWith(MockitoJUnitRunner::class)
class OfflineLocalRepositoryTest {

    @Mock
    private lateinit var repository: OfflineLocalRepository
    private val mockTipDao = mock<TipDao>()
    private val mockListDao = mock<ListDao>()
    private val userId = "test_user"

    @Before
    fun setUp() {
        repository = OfflineLocalRepository(mockTipDao, mockListDao)
    }

    @Test
    fun insertTip() = runBlocking {
        val tip = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 1, dateCreated = "10/10/2021")
        repository.insertTip(tip)
        verify(mockTipDao).insertTip(tip)
    }

    @Test
    fun deleteTip() = runBlocking {
        val tip = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 1, dateCreated = "10/10/2021")
        repository.deleteTip(tip)
        verify(mockTipDao).deleteTip(tip)
    }

    @Test
    fun updateTip() = runBlocking {
        val tip2 = Tip(id = 1, tipAmount = "200", tipPercent = "20", listId = 1, dateCreated = "10/10/2021")
        repository.updateTip(tip2)
        verify(mockTipDao).updateTip(tip2)
    }

    @Test
    fun getAllTips() = runBlocking {
        val tip1 = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 1, dateCreated = "10/10/2021")
        val tip2 = Tip(id = 2, tipAmount = "200", tipPercent = "20", listId = 1, dateCreated = "10/10/2021")
        `when`(mockTipDao.getAllTips(userId)).thenReturn(flowOf(listOf(tip1, tip2)))
        val allTips = repository.getAllTips(userId).first()
        verify(mockTipDao).getAllTips(userId)
        assertEquals(listOf(tip1, tip2), allTips)
    }

    @Test
    fun getLastTipSaved() = runBlocking {
        val tip = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 1, dateCreated = "10/10/2021")
        `when`(mockTipDao.getLastTipSaved(userId)).thenReturn(tip)
        val lastTip = repository.getLastTipSaved(userId)
        verify(mockTipDao).getLastTipSaved(userId)
        assertEquals(tip, lastTip)
    }

    @Test
    fun getAllTipsFromList() = runBlocking {
        val tip1 = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 1, dateCreated = "10/10/2021")
        val tip2 = Tip(id = 2, tipAmount = "200", tipPercent = "20", listId = 1, dateCreated = "10/10/2021")
        `when`(mockTipDao.getAllTipsFromList(1, userId)).thenReturn(flowOf(listOf(tip1, tip2)))
        val allTips = repository.getAllTipsFromList(1, userId).first()
        verify(mockTipDao).getAllTipsFromList(1, userId)
        assertEquals(listOf(tip1, tip2), allTips)
    }

    @Test
    fun insertList() = runBlocking {
        val list = List(id = 1, name = "ListTest1")
        repository.insertList(list)
        verify(mockListDao).insertList(list)
    }

    @Test
    fun deleteList() = runBlocking {
        val list = List(id = 1, name = "ListTest1")
        repository.deleteList(list)
        verify(mockListDao).deleteList(list)
    }

    @Test
    fun updateList() = runBlocking {
        val list2 = List(id = 1, name = "ListTest2")
        repository.updateList(list2)
        verify(mockListDao).updateList(list2)
    }

    @Test
    fun getAllLists() = runBlocking {
        val list1 = List(id = 1, name = "ListTest1")
        val list2 = List(id = 2, name = "ListTest2")
        `when`(mockListDao.getAllLists(userId)).thenReturn(flowOf(listOf(list1, list2)))
        val allLists = repository.getAllLists(userId).first()
        verify(mockListDao).getAllLists(userId)
        assertEquals(listOf(list1, list2), allLists)
    }

    @Test
    fun getAllListsWithTips() = runBlocking {
        val list1 = List(id = 1, name = "ListTest1")
        val list2 = List(id = 2, name = "ListTest2")
        val tip1 = Tip(id = 1, tipAmount = "100", tipPercent = "10", listId = 1, dateCreated = "10/10/2021")
        val tip2 = Tip(id = 2, tipAmount = "200", tipPercent = "20", listId = 2, dateCreated = "10/10/2021")
        val listWithTips1 = ListWithTips(list1, listOf(tip1))
        val listWithTips2 = ListWithTips(list2, listOf(tip2))
        `when`(mockListDao.getAllListsWithTips(userId))
            .thenReturn(flowOf(listOf(listWithTips1, listWithTips2)))

        val allListsWithTips = repository.getAllListsWithTips(userId).first()
        verify(mockListDao).getAllListsWithTips(userId)
        assertEquals(listOf(listWithTips1, listWithTips2), allListsWithTips)
    }

    @Test
    fun getListByName() = runBlocking {
        val list = List(id = 1, name = "ListTest1")
        `when`(mockListDao.getListByName("ListTest1", userId)).thenReturn(flowOf(list))
        val listByName = repository.getListByName("ListTest1", userId).first()
        verify(mockListDao).getListByName("ListTest1", userId)
        assertEquals(list, listByName)
    }
}
