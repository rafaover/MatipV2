package com.exercise.matipv2.ui.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Switches to a tab. The tip calculator always stays at the bottom of the stack,
 * so back from any tab returns to it and back from it leaves the app.
 */
fun MutableList<NavKey>.navigateToTopLevel(route: MatipRoute) {
    if (lastOrNull() == route) return

    clear()
    add(MatipRoute.TipCalculator)
    if (route != MatipRoute.TipCalculator) add(route)
}

/** The tab to highlight: the topmost tab in the stack, even while a dialog or settings is open over it. */
fun List<NavKey>.currentTopLevelRoute(): NavKey? =
    lastOrNull { key -> TopLevelDestination.entries.any { it.route == key } }
