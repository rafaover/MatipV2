package com.exercise.matipv2.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.exercise.matipv2.data.local.MatipDatabase
import kotlinx.coroutines.Dispatchers

/** File name of the database, the same one the app has always used. */
const val DATABASE_NAME = "matip_database"

/** Builds the database from a platform [builder], so every platform shares the same driver and settings. */
fun createMatipDatabase(builder: RoomDatabase.Builder<MatipDatabase>): MatipDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
