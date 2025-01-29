package com.exercise.matipv2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.Tip

@Database(entities = [List::class, Tip::class], version = 5)
abstract class MatipDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao
    abstract fun tipDao(): TipDao
}