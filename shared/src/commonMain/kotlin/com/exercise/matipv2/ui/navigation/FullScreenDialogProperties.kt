package com.exercise.matipv2.ui.navigation

import androidx.compose.ui.window.DialogProperties

/** Properties of the full-screen dialog that shows the tips of a list. Each platform sets up its own window. */
expect fun fullScreenDialogProperties(): DialogProperties
