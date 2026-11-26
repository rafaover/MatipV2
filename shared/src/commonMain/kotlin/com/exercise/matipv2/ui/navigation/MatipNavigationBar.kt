package com.exercise.matipv2.ui.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation3.runtime.NavKey
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** How far the glyph of a newly selected tab overshoots before it settles. */
private const val SELECTED_ICON_OVERSHOOT = 1.08f

/** Expressive bottom bar for phones. The labels name each tab for TalkBack. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MatipBottomBar(
    currentRoute: NavKey?,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShortNavigationBar(modifier = modifier) {
        TopLevelDestination.entries.forEach { destination ->
            val selected = currentRoute == destination.route
            ShortNavigationBarItem(
                selected = selected,
                onClick = { onDestinationSelected(destination) },
                icon = { TabIcon(destination = destination, selected = selected) },
                label = { Text(text = stringResource(destination.label), style = MaterialTheme.typography.labelSmall) },
            )
        }
    }
}

/** The same tabs as [MatipBottomBar], on the side, for windows wide enough to spare the width. */
@Composable
fun MatipNavigationRail(
    currentRoute: NavKey?,
    onDestinationSelected: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        TopLevelDestination.entries.forEach { destination ->
            val selected = currentRoute == destination.route
            NavigationRailItem(
                selected = selected,
                onClick = { onDestinationSelected(destination) },
                icon = { TabIcon(destination = destination, selected = selected) },
                label = { Text(text = stringResource(destination.label), style = MaterialTheme.typography.labelSmall) },
            )
        }
    }
}

/** Outline glyph, or the filled one when selected; selecting it makes it bounce once. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TabIcon(destination: TopLevelDestination, selected: Boolean) {
    val scale = remember { Animatable(1f) }
    val overshootSpec = MaterialTheme.motionScheme.fastSpatialSpec<Float>()
    val settleSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()
    LaunchedEffect(selected) {
        if (!selected) return@LaunchedEffect
        scale.animateTo(SELECTED_ICON_OVERSHOOT, overshootSpec)
        scale.animateTo(1f, settleSpec)
    }
    Icon(
        painter = painterResource(if (selected) destination.selectedIcon else destination.icon),
        // The label already names the tab for TalkBack.
        contentDescription = null,
        modifier = Modifier.graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        },
    )
}
