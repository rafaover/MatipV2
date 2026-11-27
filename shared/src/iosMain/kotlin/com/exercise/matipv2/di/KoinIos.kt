package com.exercise.matipv2.di

/** Called from Swift when the app starts, before the first screen. */
fun startKoinForIos() {
    initKoin(iosPlatformModule)
}
