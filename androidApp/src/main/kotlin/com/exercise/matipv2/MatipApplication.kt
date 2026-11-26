package com.exercise.matipv2

import android.app.Application
import com.exercise.matipv2.di.androidPlatformModule
import com.exercise.matipv2.di.initKoin
import org.koin.android.ext.koin.androidContext

class MatipApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(androidPlatformModule(BuildConfig.FIREBASE_WEB_CLIENT_ID)) {
            androidContext(this@MatipApplication)
        }
    }
}
