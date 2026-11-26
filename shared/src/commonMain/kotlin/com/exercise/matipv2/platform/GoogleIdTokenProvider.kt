package com.exercise.matipv2.platform

/** Gets the Google account token that Firebase Auth exchanges for a user. */
interface GoogleIdTokenProvider {

    /** False where Google sign in is not offered, so the app hides the button. */
    val isAvailable: Boolean

    /** Shows the account picker and returns the Google ID token. */
    suspend fun getIdToken(): Result<String>

    /** Forgets the chosen account so the picker shows again on the next sign in. */
    suspend fun clearCredentialState()
}
