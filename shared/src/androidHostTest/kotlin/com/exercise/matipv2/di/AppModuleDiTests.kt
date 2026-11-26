package com.exercise.matipv2.di

import android.content.Context
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckModulesTestL : KoinTest {

    /**
     * The modules depend on each other (repositories need the DAOs), so they
     * are verified together. Context comes from Koin's androidContext() at runtime.
     */
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        module { includes(appModule, databaseModule, androidPlatformModule(googleWebClientId = "test")) }
            .verify(extraTypes = listOf(Context::class))
    }
}
