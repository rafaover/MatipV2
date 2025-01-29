package com.exercise.matipv2.components.common

import android.content.Context
import android.content.Intent

fun shareTextWithApps(
    title: String,
    content: String,
    context: Context
) {
    val finalContent = "$title\n$content"

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, finalContent)
    }
    val intent = Intent
        .createChooser(sendIntent, null)
    context.startActivity(intent)
}