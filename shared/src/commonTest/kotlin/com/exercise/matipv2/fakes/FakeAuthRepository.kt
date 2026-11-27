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

    /** What the email calls return. Set to a failure to test error handling. */
    var emailResult: Result<Unit> = Result.success(Unit)

    /** Switches the signed-in user, like Firebase does after a sign-in or sign-out. */
    fun setUser(user: AuthUser?) {
        _currentUser.value = user
    }

    override suspend fun signIn(): Result<Unit> = Result.success(Unit)

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> = emailResult

    override suspend fun signUpWithEmail(email: String, password: String): Result<Unit> = emailResult

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = emailResult

    override suspend fun deleteAccount(): Result<Unit> = Result.success(Unit)

    override suspend fun signOut() {
        _currentUser.value = null
    }
}
