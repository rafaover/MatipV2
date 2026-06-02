package com.exercise.matipv2.data.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

interface AnalyticsHelper {
    fun logEvent(name: String, params: Bundle? = null)
    fun logScreenView(screenName: String, screenClass: String? = null)
}

class FirebaseAnalyticsHelper(context: Context) : AnalyticsHelper {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logEvent(name: String, params: Bundle?) {
        firebaseAnalytics.logEvent(name, params)
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            if (screenClass != null) {
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            }
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
