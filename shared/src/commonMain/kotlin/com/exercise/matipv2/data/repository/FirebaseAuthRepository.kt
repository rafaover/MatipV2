package com.exercise.matipv2.data.repository

import com.exercise.matipv2.data.model.AuthUser
import com.exercise.matipv2.platform.GoogleIdTokenProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FirebaseAuthRepository(
    private val googleIdTokenProvider: GoogleIdTokenProvider,
) : AuthRepository {

    private val auth = Firebase.auth

    private val _currentUser = MutableStateFlow(auth.currentUser?.toAuthUser())
    override val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    init {
        // The repository is a singleton, so this scope lives as long as the app.
        auth.authStateChanged
            .onEach { user -> _currentUser.value = user?.toAuthUser() }
            .launchIn(CoroutineScope(SupervisorJob() + Dispatchers.Default))
    }

    override suspend fun signIn(): Result<Unit> {
        val idToken = googleIdTokenProvider.getIdToken().getOrElse { return Result.failure(it) }
        return try {
            auth.signInWithCredential(GoogleAuthProvider.credential(idToken, accessToken = null))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            auth.currentUser?.delete()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        googleIdTokenProvider.clearCredentialState()
    }

    private fun FirebaseUser.toAuthUser() = AuthUser(
        id = uid,
        name = displayName,
        email = email,
        photoUrl = photoURL,
    )
}
