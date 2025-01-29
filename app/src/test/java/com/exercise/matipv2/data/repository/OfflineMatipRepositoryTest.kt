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
class OfflineMatipRepositoryTest {

    @Mock
    private lateinit var repository: OfflineMatipRepository
    private val mockTipDao = mock<TipDao>()
    private val mockListDao = mock<ListDao>()


    @Before
    fun setUp() {
        repository = OfflineMatipRepository(mockTipDao, mockListDao)
    }

    @Test
    fun insertTip() = runBlocking {
        val tip = Tip(1, "100", "10", 1, "10/10/2021")
        repository.insertTip(tip)
        verify(mockTipDao).insertTip(tip)
    }

    @Test
    fun deleteTip() = runBlocking {
        val tip = Tip(1, "100", "10", 1, "10/10/2021")
        repository.deleteTip(tip)
        verify(mockTipDao).deleteTip(tip)
    }

    @Test
    fun updateTip() = runBlocking {
        val tip1 = Tip(1, "100", "10", 1, "10/10/2021")
        repository.insertTip(tip1)
        val tip2 = Tip(1, "200", "20", 1, "10/10/2021")
        repository.updateTip(tip2)
        verify(mockTipDao).updateTip(tip2)
    }

    @Test
    fun getAllTips() = runBlocking {
        val tip1 = Tip(1, "100", "10", 1, "10/10/2021")
        val tip2 = Tip(2, "200", "20", 1, "10/10/2021")
        `when`(mockTipDao.getAllTips()).thenReturn(flowOf(listOf(tip1, tip2)))
        val allTips = repository.getAllTips().first()
        verify(mockTipDao).getAllTips()
        assertEquals(listOf(tip1, tip2), allTips)
    }

    @Test
    fun getLastTipSaved() = runBlocking {
        val tip = Tip(1, "100", "10", 1, "10/10/2021")
        `when`(mockTipDao.getLastTipSaved()).thenReturn(tip)
        val lastTip = repository.getLastTipSaved()
        verify(mockTipDao).getLastTipSaved()
        assertEquals(tip, lastTip)
    }

    @Test
    fun getAllTipsFromList() = runBlocking {
        val tip1 = Tip(1, "100", "10", 1, "10/10/2021")
        val tip2 = Tip(2, "200", "20", 1, "10/10/2021")
        `when`(mockTipDao.getAllTipsFromList(1)).thenReturn(flowOf(listOf(tip1, tip2)))
        val allTips = repository.getAllTipsFromList(1).first()
        verify(mockTipDao).getAllTipsFromList(1)
        assertEquals(listOf(tip1, tip2), allTips)
    }

    @Test
    fun insertList() = runBlocking {
        val list = List(1, "ListTest1")
        repository.insertList(list)
        verify(mockListDao).insertList(list)
    }

    @Test
    fun deleteList() = runBlocking {
        val list = List(1, "ListTest1")
        repository.deleteList(list)
        verify(mockListDao).deleteList(list)
    }

    @Test
    fun updateList() = runBlocking {
        val list1 = List(1, "ListTest1")
        repository.insertList(list1)
        val list2 = List(1, "ListTest2")
        repository.updateList(list2)
        verify(mockListDao).updateList(list2)
    }

    @Test
    fun getAllLists() = runBlocking {
        val list1 = List(1, "ListTest1")
        val list2 = List(2, "ListTest2")
        `when`(mockListDao.getAllLists()).thenReturn(flowOf(listOf(list1, list2)))
        val allLists = repository.getAllLists().first()
        verify(mockListDao).getAllLists()
        assertEquals(listOf(list1, list2), allLists)
    }

    @Test
    fun getAllListsWithTips() = runBlocking {
        val list1 = List(1, "ListTest1")
        val list2 = List(2, "ListTest2")
        val tip1 = Tip(1, "100", "10", 1, "10/10/2021")
        val tip2 = Tip(2, "200", "20", 2, "10/10/2021")
        val listWithTips1 = ListWithTips(list1, listOf(tip1))
        val listWithTips2 = ListWithTips(list2, listOf(tip2))
        `when`(mockListDao.getAllListsWithTips())
            .thenReturn(flowOf(listOf(listWithTips1, listWithTips2)))

        val allListsWithTips = repository.getAllListsWithTips().first()
        verify(mockListDao).getAllListsWithTips()
        assertEquals(listOf(listWithTips1, listWithTips2), allListsWithTips)
    }

    @Test
    fun getListByName() = runBlocking {
        val list = List(1, "ListTest1")
        `when`(mockListDao.getListByName("ListTest1")).thenReturn(flowOf(list))
        val listByName = repository.getListByName("ListTest1").first()
        verify(mockListDao).getListByName("ListTest1")
        assertEquals(list, listByName)
    }
}