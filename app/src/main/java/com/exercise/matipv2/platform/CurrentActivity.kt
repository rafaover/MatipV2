package com.exercise.matipv2.platform

import android.app.Activity

/**
 * Holds the activity that is on screen. Credential Manager and intents need an
 * activity to show their UI, but repositories must not hold one themselves.
 * [MainActivity][com.exercise.matipv2.MainActivity] sets it in onCreate and clears it in onDestroy.
 */
class CurrentActivity {
    var activity: Activity? = null
}
