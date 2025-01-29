package com.exercise.matipv2.data.repository

import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.ListWithTips
import com.exercise.matipv2.data.local.model.Tip
import kotlinx.coroutines.flow.Flow

interface MatipRepository {
    /* Tip Methods */
    suspend fun insertTip(tip: Tip)
    suspend fun deleteTip(tip: Tip)
    suspend fun updateTip(tip: Tip)
    fun getAllTips(): Flow<kotlin.collections.List<Tip>>
    suspend fun getLastTipSaved(): Tip
    fun getAllTipsFromList(listId: Int): Flow<kotlin.collections.List<Tip>>

    /* List Methods */
    suspend fun insertList(list: List)
    suspend fun deleteList(list: List)
    suspend fun updateList(list: List)
    fun getAllLists(): Flow<kotlin.collections.List<List>>
    fun getListByName(listName: String): Flow<List>
    fun getListById(listId: Int): Flow<List>
    fun getAllListsWithTips(): Flow<kotlin.collections.List<ListWithTips>>
}