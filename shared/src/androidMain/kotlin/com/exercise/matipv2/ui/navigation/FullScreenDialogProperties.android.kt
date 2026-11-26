package com.exercise.matipv2.ui.navigation

import android.os.Build
import androidx.compose.ui.window.DialogProperties

actual fun fullScreenDialogProperties() = DialogProperties(
    // Above API 34 the platform default width keeps the dialog from overshooting the bottom of the screen.
    usePlatformDefaultWidth = Build.VERSION.SDK_INT > Build.VERSION_CODES.UPSIDE_DOWN_CAKE,
    dismissOnBackPress = true,
    decorFitsSystemWindows = false,
    dismissOnClickOutside = false
)
