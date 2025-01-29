package com.exercise.matipv2.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideListDao(get()) }
    single { provideTipDao(get()) }
}