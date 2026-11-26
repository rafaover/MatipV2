package com.exercise.matipv2.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.exercise.matipv2.data.local.dao.ListDao
import com.exercise.matipv2.data.local.dao.TipDao
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.local.model.Tip

@Database(entities = [TipList::class, Tip::class], version = 7)
@ConstructedBy(MatipDatabaseConstructor::class)
abstract class MatipDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao
    abstract fun tipDao(): TipDao
}

/** Room generates the `actual` of this constructor for each platform. */
@Suppress("KotlinNoActualForExpect")
expect object MatipDatabaseConstructor : RoomDatabaseConstructor<MatipDatabase> {
    override fun initialize(): MatipDatabase
}
