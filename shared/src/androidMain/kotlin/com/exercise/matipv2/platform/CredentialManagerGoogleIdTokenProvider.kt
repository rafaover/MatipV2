package com.exercise.matipv2.platform

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class CredentialManagerGoogleIdTokenProvider(
    applicationContext: Context,
    private val currentActivity: CurrentActivity,
    private val googleWebClientId: String,
) : GoogleIdTokenProvider {

    private val credentialManager = CredentialManager.create(applicationContext)

    override suspend fun getIdToken(): Result<String> {
        val activity = currentActivity.activity
            ?: return Result.failure(IllegalStateException("No activity available to sign in"))

        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts = false)
                .setServerClientId(googleWebClientId)
                .setAutoSelectEnabled(autoSelectEnabled = true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context = activity, request = request)
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            Result.success(googleIdTokenCredential.idToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearCredentialState() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}
