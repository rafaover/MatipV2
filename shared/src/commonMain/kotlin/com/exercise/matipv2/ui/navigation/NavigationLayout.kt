package com.exercise.matipv2.ui.navigation

import androidx.compose.ui.unit.Dp
import com.exercise.matipv2.ui.theme.MatipDimens

/** Where the tabs live: along the bottom on phones, in a side rail on wide windows. */
enum class NavigationLayout { BottomBar, Rail }

fun navigationLayoutFor(windowWidth: Dp): NavigationLayout =
    if (windowWidth >= MatipDimens.railMinWidth) NavigationLayout.Rail else NavigationLayout.BottomBar
