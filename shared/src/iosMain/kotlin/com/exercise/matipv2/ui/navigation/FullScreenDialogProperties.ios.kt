package com.exercise.matipv2.ui.navigation

import androidx.compose.ui.window.DialogProperties

actual fun fullScreenDialogProperties() = DialogProperties(
    dismissOnBackPress = true,
    dismissOnClickOutside = false,
    usePlatformDefaultWidth = false
)
