package com.exercise.matipv2.fakes

import com.exercise.matipv2.data.local.model.ListWithTips
import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** In-memory [LocalRepository] for tests. Tips and lists are kept in [tips] and [lists]. */
class FakeLocalRepository : LocalRepository {

    val tips = MutableStateFlow<List<Tip>>(emptyList())
    val lists = MutableStateFlow<List<TipList>>(emptyList())

    private var nextTipId = 1
    private var nextListId = 1

    override suspend fun insertTip(tip: Tip): Long {
        val id = if (tip.id == 0) nextTipId++ else tip.id
        tips.value += tip.copy(id = id)
        return id.toLong()
    }

    override suspend fun deleteTip(tip: Tip) {
        tips.value = tips.value.filterNot { it.id == tip.id }
    }

    override suspend fun updateTip(tip: Tip) {
        tips.value = tips.value.map { if (it.id == tip.id) tip else it }
    }

    override fun getAllTips(userId: String?): Flow<List<Tip>> =
        tips.map { all -> all.filter { it.userId == userId } }

    override suspend fun getLastTipSaved(userId: String?): Tip =
        tips.value.last { it.userId == userId }

    override fun getAllTipsFromList(listId: Int, userId: String?): Flow<List<Tip>> =
        getAllTips(userId).map { all -> all.filter { it.listId == listId } }

    override fun searchTipsInList(listId: Int, query: String, userId: String?): Flow<List<Tip>> =
        getAllTipsFromList(listId, userId).map { all -> all.filter { query in it.tipAmount } }

    override suspend fun insertList(list: TipList): Long {
        val id = if (list.id == 0) nextListId++ else list.id
        lists.value += list.copy(id = id)
        return id.toLong()
    }

    override suspend fun deleteList(list: TipList) {
        lists.value = lists.value.filterNot { it.id == list.id }
    }

    override suspend fun updateList(list: TipList) {
        lists.value = lists.value.map { if (it.id == list.id) list else it }
    }

    override fun getAllLists(userId: String?): Flow<List<TipList>> =
        lists.map { all -> all.filter { it.userId == userId } }

    override fun getListByName(listName: String, userId: String?): Flow<TipList> =
        getAllLists(userId).map { all -> all.first { it.name == listName } }

    override fun getListById(listId: Int, userId: String?): Flow<TipList> =
        getAllLists(userId).map { all -> all.first { it.id == listId } }

    override fun getAllListsWithTips(userId: String?): Flow<List<ListWithTips>> =
        getAllLists(userId).map { all ->
            all.map { list -> ListWithTips(list, tips.value.filter { it.listId == list.id }) }
        }

    override fun searchLists(query: String, userId: String?): Flow<List<TipList>> =
        getAllLists(userId).map { all -> all.filter { query in it.name } }

    override suspend fun migrateGuestData(userId: String) {
        lists.value = lists.value.map { if (it.userId == null) it.copy(userId = userId) else it }
        tips.value = tips.value.map { if (it.userId == null) it.copy(userId = userId) else it }
    }

    override suspend fun deleteAllUserData(userId: String) {
        lists.value = lists.value.filterNot { it.userId == userId }
        tips.value = tips.value.filterNot { it.userId == userId }
    }
}
