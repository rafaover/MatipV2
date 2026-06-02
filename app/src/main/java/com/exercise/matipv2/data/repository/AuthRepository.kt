package com.exercise.matipv2.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    val currentUser: StateFlow<AuthUser?>
    suspend fun signIn(context: Context): Result<Unit>
    suspend fun signOut()
}

data class AuthUser(
    val id: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?
)

class FirebaseAuthRepository(
    context: Context
) : AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    private val _currentUser = MutableStateFlow<AuthUser?>(null)
    override val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser?.let {
                AuthUser(
                    id = it.uid,
                    name = it.displayName,
                    email = it.email,
                    photoUrl = it.photoUrl?.toString()
                )
            }
        }
    }

    override suspend fun signIn(context: Context): Result<Unit> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("YOUR_WEB_CLIENT_ID_HERE") // TODO: Move to string resources
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            
            auth.signInWithCredential(authCredential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}
