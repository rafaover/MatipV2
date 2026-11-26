package com.exercise.matipv2.fakes

import com.exercise.matipv2.data.model.AuthUser
import com.exercise.matipv2.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** [AuthRepository] for tests: every call succeeds and nothing leaves the process. */
class FakeAuthRepository(signedInUser: AuthUser? = null) : AuthRepository {

    private val _currentUser = MutableStateFlow(signedInUser)
    override val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    override suspend fun signIn(): Result<Unit> = Result.success(Unit)

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> = Result.success(Unit)

    override suspend fun signUpWithEmail(email: String, password: String): Result<Unit> = Result.success(Unit)

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = Result.success(Unit)

    override suspend fun deleteAccount(): Result<Unit> = Result.success(Unit)

    override suspend fun signOut() {
        _currentUser.value = null
    }
}
