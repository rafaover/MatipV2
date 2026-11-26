package com.exercise.matipv2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.local.model.ListWithTips
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(list: TipList): Long

    @Delete
    suspend fun deleteList(list: TipList)

    @Update
    suspend fun updateList(list: TipList)

    @Query("SELECT * FROM lists WHERE user_id IS :userId")
    fun getAllLists(userId: String?): Flow<List<TipList>>

    @Query("SELECT * FROM lists WHERE list_name = :listName AND user_id IS :userId")
    fun getListByName(listName: String, userId: String?): Flow<TipList>

    @Query("SELECT * FROM lists WHERE id = :listId AND user_id IS :userId")
    fun getListById(listId: Int, userId: String?): Flow<TipList>

    @Transaction
    @Query("""
        SELECT * FROM lists 
        INNER JOIN tips ON lists.id = tips.list_id
        WHERE lists.user_id IS :userId
        GROUP BY lists.id
        """)
    fun getAllListsWithTips(userId: String?): Flow<List<ListWithTips>>

    /**
     * Search for lists where the list name contains the given query string.
     */
    @Query("SELECT * FROM lists WHERE list_name LIKE '%' || :query || '%' AND user_id IS :userId")
    fun searchLists(query: String, userId: String?): Flow<List<TipList>>

    @Query("UPDATE lists SET user_id = :userId WHERE user_id IS NULL")
    suspend fun migrateGuestLists(userId: String)

    @Query("DELETE FROM lists WHERE user_id IS :userId")
    suspend fun deleteAllListsForUser(userId: String)
}