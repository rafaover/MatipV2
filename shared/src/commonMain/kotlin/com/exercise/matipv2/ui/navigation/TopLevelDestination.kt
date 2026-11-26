package com.exercise.matipv2.ui.navigation

import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.ic_home
import com.exercise.matipv2.generated.resources.ic_home_outlined
import com.exercise.matipv2.generated.resources.ic_view_list
import com.exercise.matipv2.generated.resources.ic_view_list_outlined
import com.exercise.matipv2.generated.resources.list_tab
import com.exercise.matipv2.generated.resources.tipping_tab
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/** The tabs of the bottom bar (or side rail), in display order. */
enum class TopLevelDestination(
    val route: MatipRoute,
    val label: StringResource,
    /** Outline glyph, shown when the tab is not selected. */
    val icon: DrawableResource,
    /** Filled glyph, shown when the tab is selected. */
    val selectedIcon: DrawableResource,
) {
    TipCalculator(
        MatipRoute.TipCalculator,
        Res.string.tipping_tab,
        Res.drawable.ic_home_outlined,
        Res.drawable.ic_home,
    ),
    Lists(
        MatipRoute.Lists,
        Res.string.list_tab,
        Res.drawable.ic_view_list_outlined,
        Res.drawable.ic_view_list,
    ),
}
