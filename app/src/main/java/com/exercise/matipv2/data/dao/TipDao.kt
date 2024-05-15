package com.exercise.matipv2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.exercise.matipv2.data.model.Tip
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTip(tip: Tip)

    @Delete
    suspend fun deleteTip(tip: Tip)

    @Update
    suspend fun updateTip(tip: Tip)

    @Query("SELECT * FROM tips")
    fun getAllTips(): Flow<List<Tip>>

    @Query("UPDATE tips SET event_id = :eventId WHERE id = :tipId")
    suspend fun addTipToEvent(tipId: Int, eventId: Int)

    @Query("SELECT * FROM tips ORDER BY id DESC LIMIT 1")
    suspend fun getLastTipSaved(): Tip
}