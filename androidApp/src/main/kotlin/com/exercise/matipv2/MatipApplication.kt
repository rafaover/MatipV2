package com.exercise.matipv2

import android.app.Application
import com.exercise.matipv2.di.androidPlatformModule
import com.exercise.matipv2.di.appModule
import com.exercise.matipv2.di.databaseModule
import com.exercise.matipv2.ui.navigation.NavBarItems
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MatipApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MatipApplication)
            modules(
                databaseModule,
                androidPlatformModule(BuildConfig.FIREBASE_WEB_CLIENT_ID),
                appModule
            )
        }

        NavBarItems.values
    }
}