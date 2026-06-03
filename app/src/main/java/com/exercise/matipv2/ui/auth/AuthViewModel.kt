package com.exercise.matipv2.ui.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.matipv2.data.analytics.AnalyticsHelper
import com.exercise.matipv2.data.repository.AuthRepository
import com.exercise.matipv2.data.repository.MatipRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModel(
    private val authRepository: AuthRepository,
    private val matipRepository: MatipRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    val currentUser = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val lastBackupDate: StateFlow<String?> = currentUser.flatMapLatest { user ->
        if (user != null) {
            matipRepository.getLastBackupDate(user.id)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var showAuthDialog by mutableStateOf(value = false)
        private set

    var isLoading by mutableStateOf(value = false)
        private set

    fun updateShowAuthDialog(show: Boolean) {
        showAuthDialog = show
    }

    fun signIn(context: Context, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authRepository.signIn(context).onSuccess {
                currentUser.value?.id?.let { userId ->
                    matipRepository.migrateGuestData(userId)
                    analyticsHelper.logEvent("user_signed_in_and_migrated")
                }
            }.onFailure { 
                onError(it.message ?: "Auth Error") 
            }
            isLoading = false
        }
    }

    fun signInWithEmail(email: String, password: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authRepository.signInWithEmail(email, password).onSuccess {
                currentUser.value?.id?.let { userId ->
                    matipRepository.migrateGuestData(userId)
                    analyticsHelper.logEvent("user_signed_in_and_migrated")
                }
            }.onFailure { 
                onError(it.message ?: "Auth Error") 
            }
            isLoading = false
        }
    }

    fun signUpWithEmail(email: String, password: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authRepository.signUpWithEmail(email, password).onSuccess {
                currentUser.value?.id?.let { userId ->
                    matipRepository.migrateGuestData(userId)
                    analyticsHelper.logEvent("user_signed_up_and_migrated")
                }
            }.onFailure {
                onError(it.message ?: "Auth Error") 
            }
            isLoading = false
        }
    }

    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authRepository.sendPasswordResetEmail(email)
                .onSuccess { onSuccess() }
                .onFailure { onError(it.message ?: "Error") }
            isLoading = false
        }
    }

    fun backupData(onError: (String) -> Unit) {
        viewModelScope.launch {
            currentUser.value?.id?.let { userId ->
                isLoading = true
                matipRepository.backupDataToCloud(userId)
                    .onSuccess { analyticsHelper.logEvent("cloud_backup_success") }
                    .onFailure { onError(it.message ?: "Backup failed") }
                isLoading = false
            }
        }
    }

    fun restoreData(onError: (String) -> Unit) {
        viewModelScope.launch {
            currentUser.value?.id?.let { userId ->
                isLoading = true
                matipRepository.restoreDataFromCloud(userId)
                    .onSuccess { analyticsHelper.logEvent("cloud_restore_success") }
                    .onFailure { onError(it.message ?: "Restore failed") }
                isLoading = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            analyticsHelper.logEvent("user_signed_out")
        }
    }
}
