package com.exercise.matipv2.platform

/**
 * Google sign in is not offered on iOS: Apple requires Sign in with Apple next to any other
 * third-party login, so iOS keeps email and password only.
 */
class IosGoogleIdTokenProvider : GoogleIdTokenProvider {

    override val isAvailable: Boolean = false

    override suspend fun getIdToken(): Result<String> =
        Result.failure(UnsupportedOperationException("Google sign in is not offered on iOS"))

    override suspend fun clearCredentialState() = Unit
}
