package com.exercise.matipv2.di

import com.exercise.matipv2.platform.GoogleIdTokenProvider
import com.exercise.matipv2.platform.IosGoogleIdTokenProvider
import com.exercise.matipv2.platform.IosPlatformActions
import com.exercise.matipv2.platform.PlatformActions
import org.koin.dsl.module

/** iOS implementations of the platform interfaces. */
val iosPlatformModule = module {
    single { matipDatabaseBuilder() }
    single<PlatformActions> { IosPlatformActions() }
    single<GoogleIdTokenProvider> { IosGoogleIdTokenProvider() }
}
