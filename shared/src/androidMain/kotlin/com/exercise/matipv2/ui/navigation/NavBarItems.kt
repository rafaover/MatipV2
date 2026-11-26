package com.exercise.matipv2.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.list_tab
import com.exercise.matipv2.generated.resources.tipping_tab

sealed class NavBarItems(
    val title: StringResource,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object TipCalculator : NavBarItems(
        Res.string.tipping_tab,
        "tipCalculator",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    object Lists : NavBarItems(
        Res.string.list_tab,
        "lists",
        selectedIcon = Icons.AutoMirrored.Filled.ViewList,
        unselectedIcon = Icons.AutoMirrored.Outlined.ViewList
    )

    companion object {
        val values = listOf(TipCalculator, Lists)
    }
}