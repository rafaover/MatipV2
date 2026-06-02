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
    override fun getAllTips(userId: String?) = tipDao.getAllTips(userId)
    override suspend fun getLastTipSaved(userId: String?): Tip {
        return tipDao.getLastTipSaved(userId)
    }
    override fun getAllTipsFromList(listId: Int, userId: String?): Flow<kotlin.collections.List<Tip>> {
        return tipDao.getAllTipsFromList(listId, userId)
    }

    override fun searchTipsInList(listId: Int, query: String, userId: String?) = tipDao.searchTipsInList(listId, query, userId)


    // List Methods
    override suspend fun insertList(list: List) = listDao.insertList(list)
    override suspend fun deleteList(list: List) = listDao.deleteList(list)
    override suspend fun updateList(list: List) = listDao.updateList(list)
    override fun getAllLists(userId: String?) = listDao.getAllLists(userId)
    override fun getAllListsWithTips(userId: String?): Flow<kotlin.collections.List<ListWithTips>> {
        return listDao.getAllListsWithTips(userId)
    }
    override fun getListByName(listName: String, userId: String?): Flow<List> {
        return listDao.getListByName(listName, userId)
    }
    override fun getListById(listId: Int, userId: String?): Flow<List> {
        return listDao.getListById(listId, userId)
    }
    override fun searchLists(query: String, userId: String?) = listDao.searchLists(query, userId)

    override suspend fun migrateGuestData(userId: String) {
        listDao.migrateGuestLists(userId)
        tipDao.migrateGuestTips(userId)
    }
}