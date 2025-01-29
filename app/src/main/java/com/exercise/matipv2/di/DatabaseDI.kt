package com.exercise.matipv2.di

import android.content.Context
import androidx.room.Room
import com.exercise.matipv2.data.local.MatipDatabase

fun provideDatabase(context: Context): MatipDatabase {
    return Room.databaseBuilder(context, MatipDatabase::class.java, "matip_database")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideListDao(database: MatipDatabase) = database.listDao()

fun provideTipDao(database: MatipDatabase) = database.tipDao()