package com.exercise.matipv2.ui.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.matipv2.data.analytics.AnalyticsHelper
import com.exercise.matipv2.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    val currentUser = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var showAuthDialog by mutableStateOf(value = false)
        private set

    fun updateShowAuthDialog(show: Boolean) {
        showAuthDialog = show
    }

    fun signIn(context: Context, onError: (String) -> Unit) {
        viewModelScope.launch {
            authRepository.signIn(context).onFailure { 
                onError(it.message ?: "Auth Error") 
            }
        }
    }

    fun signInWithEmail(email: String, password: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            authRepository.signInWithEmail(email, password).onFailure { 
                onError(it.message ?: "Auth Error") 
            }
        }
    }

    fun signUpWithEmail(email: String, password: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            authRepository.signUpWithEmail(email, password).onFailure { 
                onError(it.message ?: "Auth Error") 
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
