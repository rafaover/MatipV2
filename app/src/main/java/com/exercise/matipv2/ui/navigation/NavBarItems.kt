package com.exercise.matipv2.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.exercise.matipv2.R

sealed class NavBarItems(
    @StringRes val title: Int,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object TipCalculator : NavBarItems(
        R.string.tipping_tab,
        "tipCalculator",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    object Lists : NavBarItems(
        R.string.list_tab,
        "lists",
        selectedIcon = Icons.AutoMirrored.Filled.ViewList,
        unselectedIcon = Icons.AutoMirrored.Outlined.ViewList
    )

    companion object {
        val values = listOf(TipCalculator, Lists)
    }
}