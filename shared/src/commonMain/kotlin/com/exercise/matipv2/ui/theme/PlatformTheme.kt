package com.exercise.matipv2.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/** The device's dynamic color scheme, or null when the platform has none. */
@Composable
expect fun dynamicColorSchemeOrNull(darkTheme: Boolean): ColorScheme?

/** Matches the system bar icons to the theme. */
@Composable
expect fun SystemBarsAppearance(darkTheme: Boolean)
