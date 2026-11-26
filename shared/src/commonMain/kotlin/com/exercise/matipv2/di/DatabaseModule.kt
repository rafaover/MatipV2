package com.exercise.matipv2.di

import com.exercise.matipv2.data.local.MatipDatabase
import org.koin.dsl.module

/** Needs a `RoomDatabase.Builder<MatipDatabase>` from the platform module. */
val databaseModule = module {
    single { createMatipDatabase(get()) }
    single { get<MatipDatabase>().listDao() }
    single { get<MatipDatabase>().tipDao() }
}
