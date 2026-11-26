package com.exercise.matipv2.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.exercise.matipv2.data.local.MatipDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

/** Keeps the database in the app's documents folder. */
fun matipDatabaseBuilder(name: String = DATABASE_NAME): RoomDatabase.Builder<MatipDatabase> {
    val documentsFolder = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        .first() as String
    return Room.databaseBuilder<MatipDatabase>(name = "$documentsFolder/$name")
        .setQueryCoroutineContext(Dispatchers.IO)
}
