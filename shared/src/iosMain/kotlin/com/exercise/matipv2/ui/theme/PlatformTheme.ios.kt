package com.exercise.matipv2.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/** iOS has no dynamic color, the app's own colors are used. */
@Composable
actual fun dynamicColorSchemeOrNull(darkTheme: Boolean): ColorScheme? = null

/** The status bar follows the system on iOS, nothing to set up. */
@Composable
actual fun SystemBarsAppearance(darkTheme: Boolean) = Unit
