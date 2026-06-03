package com.exercise.matipv2.ui.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.matipv2.data.analytics.AnalyticsHelper
import com.exercise.matipv2.data.repository.AuthRepository
import com.exercise.matipv2.data.repository.BackupRepository
import com.exercise.matipv2.data.repository.LocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModel(
    private val authRepository: AuthRepository,
    private val localRepository: LocalRepository,
    private val backupRepository: BackupRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    val currentUser = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val lastBackupDate: StateFlow<String?> = currentUser.flatMapLatest { user ->
        if (user != null) {
            backupRepository.getLastBackupDate(user.id)
                .catch { emit(null) }
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var showAuthDialog by mutableStateOf(value = false)
        private set

    var showDeleteAccountDialog by mutableStateOf(value = false)
        private set

    var isLoading by mutableStateOf(value = false)
        private set

    fun updateShowAuthDialog(show: Boolean) {
        showAuthDialog = show
    }

    fun updateShowDeleteAccountDialog(show: Boolean) {
        showDeleteAccountDialog = show
    }

    fun signIn(context: Context, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            authRepository.signIn(context).onSuccess {
                currentUser.value?.id?.let { userId ->
                    localRepository.migrateGuestData(userId)
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
                    localRepository.migrateGuestData(userId)
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
                    localRepository.migrateGuestData(userId)
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

    fun backupData(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            currentUser.value?.id?.let { userId ->
                isLoading = true
                backupRepository.backupDataToCloud(userId)
                    .onSuccess { 
                        analyticsHelper.logEvent("cloud_backup_success")
                        onSuccess()
                    }
                    .onFailure { onError(it.message ?: "Backup failed") }
                isLoading = false
            }
        }
    }

    fun restoreData(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            currentUser.value?.id?.let { userId ->
                isLoading = true
                backupRepository.restoreDataFromCloud(userId)
                    .onSuccess { 
                        analyticsHelper.logEvent("cloud_restore_success")
                        onSuccess()
                    }
                    .onFailure { onError(it.message ?: "Restore failed") }
                isLoading = false
            }
        }
    }

    fun deleteAccount(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            currentUser.value?.id?.let { userId ->
                isLoading = true
                // 1. Delete Cloud Data
                backupRepository.deleteCloudData(userId).onSuccess {
                    // 2. Delete Local Data
                    localRepository.deleteAllUserData(userId)
                    // 3. Delete Firebase Account
                    authRepository.deleteAccount().onSuccess {
                        analyticsHelper.logEvent("account_deleted")
                        onSuccess()
                    }.onFailure { onError(it.message ?: "Failed to delete account") }
                }.onFailure { onError(it.message ?: "Failed to delete cloud data") }
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
