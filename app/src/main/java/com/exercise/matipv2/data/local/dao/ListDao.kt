package com.exercise.matipv2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Transaction
import androidx.room.Update
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.ListWithTips
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(list: List)

    @Delete
    suspend fun deleteList(list: List)

    @Update
    suspend fun updateList(list: List)

    @Query("SELECT * FROM lists")
    fun getAllLists(): Flow<kotlin.collections.List<List>>

    @Query("SELECT * FROM lists WHERE list_name = :listName")
    fun getListByName(listName: String): Flow<List>

    @Query("SELECT * FROM lists WHERE id = :listId")
    fun getListById(listId: Int): Flow<List>

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("""
        SELECT * FROM lists 
        INNER JOIN tips ON lists.id = tips.list_id
        GROUP BY lists.id
        """)
    fun getAllListsWithTips(): Flow<kotlin.collections.List<ListWithTips>>
}