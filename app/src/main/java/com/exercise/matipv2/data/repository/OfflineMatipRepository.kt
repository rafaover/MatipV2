package com.exercise.matipv2.data.repository

import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.ListWithTips
import com.exercise.matipv2.data.local.model.Tip
import kotlinx.coroutines.flow.Flow

class OfflineMatipRepository (
    private val tipDao: TipDao,
    private val listDao: ListDao
) : MatipRepository {

    // Tip Methods
    override suspend fun insertTip(tip: Tip) = tipDao.insertTip(tip)
    override suspend fun deleteTip(tip: Tip) = tipDao.deleteTip(tip)
    override suspend fun updateTip(tip: Tip) = tipDao.updateTip(tip)
    override fun getAllTips() = tipDao.getAllTips()
    override suspend fun getLastTipSaved(): Tip {
        return tipDao.getLastTipSaved()
    }
    override fun getAllTipsFromList(listId: Int): Flow<kotlin.collections.List<Tip>> {
        return tipDao.getAllTipsFromList(listId)
    }

    // List Methods
    override suspend fun insertList(list: List) = listDao.insertList(list)
    override suspend fun deleteList(list: List) = listDao.deleteList(list)
    override suspend fun updateList(list: List) = listDao.updateList(list)
    override fun getAllLists() = listDao.getAllLists()
    override fun getAllListsWithTips(): Flow<kotlin.collections.List<ListWithTips>> {
        return listDao.getAllListsWithTips()
    }
    override fun getListByName(listName: String): Flow<List> {
        return listDao.getListByName(listName)
    }
    override fun getListById(listId: Int): Flow<List> {
        return listDao.getListById(listId)
    }
}