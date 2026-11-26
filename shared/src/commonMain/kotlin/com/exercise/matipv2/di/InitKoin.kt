package com.exercise.matipv2.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Starts Koin with every shared module plus the platform [platformModules].
 * [configuration] is for platform setup, like Android's `androidContext`.
 */
fun initKoin(vararg platformModules: Module, configuration: KoinApplication.() -> Unit = {}): KoinApplication =
    startKoin {
        configuration()
        modules(databaseModule, appModule, *platformModules)
    }
