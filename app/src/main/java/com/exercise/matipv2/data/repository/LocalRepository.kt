package com.exercise.matipv2.data.repository

import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.local.model.ListWithTips
import com.exercise.matipv2.data.local.model.Tip
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun insertTip(tip: Tip): Long
    suspend fun deleteTip(tip: Tip)
    suspend fun updateTip(tip: Tip)
    fun getAllTips(userId: String?): Flow<List<Tip>>
    suspend fun getLastTipSaved(userId: String?): Tip
    fun getAllTipsFromList(listId: Int, userId: String?): Flow<List<Tip>>
    fun searchTipsInList(listId: Int, query: String, userId: String?): Flow<List<Tip>>

    suspend fun insertList(list: TipList): Long
    suspend fun deleteList(list: TipList)
    suspend fun updateList(list: TipList)
    fun getAllLists(userId: String?): Flow<List<TipList>>
    fun getListByName(listName: String, userId: String?): Flow<TipList>
    fun getListById(listId: Int, userId: String?): Flow<TipList>
    fun getAllListsWithTips(userId: String?): Flow<List<ListWithTips>>
    fun searchLists(query: String, userId: String?): Flow<List<TipList>>

    suspend fun migrateGuestData(userId: String)
    suspend fun deleteAllUserData(userId: String)
}

class OfflineLocalRepository(
    private val tipDao: TipDao,
    private val listDao: ListDao
) : LocalRepository {
    override suspend fun insertTip(tip: Tip) = tipDao.insertTip(tip)
    override suspend fun deleteTip(tip: Tip) = tipDao.deleteTip(tip)
    override suspend fun updateTip(tip: Tip) = tipDao.updateTip(tip)
    override fun getAllTips(userId: String?) = tipDao.getAllTips(userId)
    override suspend fun getLastTipSaved(userId: String?): Tip = tipDao.getLastTipSaved(userId)
    override fun getAllTipsFromList(listId: Int, userId: String?) = tipDao.getAllTipsFromList(listId, userId)
    override fun searchTipsInList(listId: Int, query: String, userId: String?) = tipDao.searchTipsInList(listId, query, userId)

    override suspend fun insertList(list: TipList) = listDao.insertList(list)
    override suspend fun deleteList(list: TipList) = listDao.deleteList(list)
    override suspend fun updateList(list: TipList) = listDao.updateList(list)
    override fun getAllLists(userId: String?) = listDao.getAllLists(userId)
    override fun getListByName(listName: String, userId: String?) = listDao.getListByName(listName, userId)
    override fun getListById(listId: Int, userId: String?) = listDao.getListById(listId, userId)
    override fun getAllListsWithTips(userId: String?) = listDao.getAllListsWithTips(userId)
    override fun searchLists(query: String, userId: String?) = listDao.searchLists(query, userId)

    override suspend fun migrateGuestData(userId: String) {
        listDao.migrateGuestLists(userId)
        tipDao.migrateGuestTips(userId)
    }

    override suspend fun deleteAllUserData(userId: String) {
        tipDao.deleteAllTipsForUser(userId)
        listDao.deleteAllListsForUser(userId)
    }
}
