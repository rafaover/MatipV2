package com.exercise.matipv2.di

import com.exercise.matipv2.data.analytics.AnalyticsHelper
import com.exercise.matipv2.data.analytics.FirebaseAnalyticsHelper
import com.exercise.matipv2.data.repository.AuthRepository
import com.exercise.matipv2.data.repository.BackupRepository
import com.exercise.matipv2.data.repository.FirebaseAuthRepository
import com.exercise.matipv2.data.repository.FirestoreBackupRepository
import com.exercise.matipv2.data.repository.LocalRepository
import com.exercise.matipv2.data.repository.OfflineLocalRepository
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.ui.auth.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::OfflineLocalRepository) { bind<LocalRepository>() }
    singleOf(::FirestoreBackupRepository) { bind<BackupRepository>() }
    single<AuthRepository> { FirebaseAuthRepository(androidContext()) }
    single<AnalyticsHelper> { FirebaseAnalyticsHelper(androidContext()) }
    single { FirebaseFirestore.getInstance() }
    viewModelOf(::MainScreenViewModel)
    viewModelOf(::AuthViewModel)
}
