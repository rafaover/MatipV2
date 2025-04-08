package com.exercise.matipv2.di

import com.exercise.matipv2.data.repository.MatipRepository
import com.exercise.matipv2.data.repository.OfflineMatipRepository
import com.exercise.matipv2.ui.MainScreenViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::OfflineMatipRepository) { bind<MatipRepository>() }
    viewModelOf(::MainScreenViewModel)
}