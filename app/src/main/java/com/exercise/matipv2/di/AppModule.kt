package com.exercise.matipv2.di

import com.exercise.matipv2.data.repository.MatipRepository
import com.exercise.matipv2.data.repository.OfflineMatipRepository
import com.exercise.matipv2.ui.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<MatipRepository> {
        OfflineMatipRepository(tipDao = get(), listDao = get())
    }
    viewModel() { MainScreenViewModel(get()) }
}