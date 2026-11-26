package com.exercise.matipv2.platform

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class AndroidPlatformActions(
    private val applicationContext: Context,
    private val currentActivity: CurrentActivity,
) : PlatformActions {

    override fun shareText(title: String, content: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = MIME_TYPE_TEXT
            putExtra(Intent.EXTRA_TEXT, "$title\n$content")
        }
        launch(Intent.createChooser(sendIntent, null))
    }

    override fun sendEmail(address: String, subject: String): Boolean {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$address".toUri()
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        return launch(intent)
    }

    override fun openUrl(url: String): Boolean = launch(Intent(Intent.ACTION_VIEW, url.toUri()))

    override fun openStoreListing() {
        val packageName = applicationContext.packageName
        val openedInStoreApp = launch(
            Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        )
        if (!openedInStoreApp) {
            openUrl("https://play.google.com/store/apps/details?id=$packageName")
        }
    }

    /**
     * Starts [intent] from the visible activity when there is one. Without an
     * activity the intent needs its own task. Returns false if nothing handles it.
     */
    private fun launch(intent: Intent): Boolean {
        val activity = currentActivity.activity
        if (activity == null) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val launcher: Context = activity ?: applicationContext
        return try {
            launcher.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }

    private companion object {
        const val MIME_TYPE_TEXT = "text/plain"
    }
}
