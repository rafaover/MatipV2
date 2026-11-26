package com.exercise.matipv2.di

import com.exercise.matipv2.platform.AndroidPlatformActions
import com.exercise.matipv2.platform.CredentialManagerGoogleIdTokenProvider
import com.exercise.matipv2.platform.CurrentActivity
import com.exercise.matipv2.platform.GoogleIdTokenProvider
import com.exercise.matipv2.platform.PlatformActions
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** Android implementations of the platform interfaces. [googleWebClientId] comes from the app module. */
fun androidPlatformModule(googleWebClientId: String) = module {
    single { matipDatabaseBuilder(androidContext()) }
    single { CurrentActivity() }
    single<PlatformActions> { AndroidPlatformActions(androidContext(), get()) }
    single<GoogleIdTokenProvider> {
        CredentialManagerGoogleIdTokenProvider(androidContext(), get(), googleWebClientId)
    }
}
