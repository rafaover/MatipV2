package com.exercise.matipv2.ui.navigation

import com.exercise.matipv2.generated.resources.ic_home
import com.exercise.matipv2.generated.resources.ic_home_outlined
import com.exercise.matipv2.generated.resources.ic_view_list
import com.exercise.matipv2.generated.resources.ic_view_list_outlined
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.list_tab
import com.exercise.matipv2.generated.resources.tipping_tab

sealed class NavBarItems(
    val title: StringResource,
    val route: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource
) {
    object TipCalculator : NavBarItems(
        Res.string.tipping_tab,
        "tipCalculator",
        selectedIcon = Res.drawable.ic_home,
        unselectedIcon = Res.drawable.ic_home_outlined
    )
    object Lists : NavBarItems(
        Res.string.list_tab,
        "lists",
        selectedIcon = Res.drawable.ic_view_list,
        unselectedIcon = Res.drawable.ic_view_list_outlined
    )

    companion object {
        val values = listOf(TipCalculator, Lists)
    }
}