package com.exercise.matipv2.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import com.exercise.matipv2.data.local.MatipDatabase

/** Opens the same `databases/matip_database` file the app has always used. */
fun matipDatabaseBuilder(context: Context, name: String = DATABASE_NAME): RoomDatabase.Builder<MatipDatabase> {
    val appContext = context.applicationContext
    return Room.databaseBuilder<MatipDatabase>(
        context = appContext,
        name = appContext.getDatabasePath(name).absolutePath
    ).setQueryCoroutineContext(Dispatchers.IO)
}
