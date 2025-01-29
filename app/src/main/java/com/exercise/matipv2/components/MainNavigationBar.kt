package com.exercise.matipv2.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.exercise.matipv2.ui.navigation.NavBarItems

@Composable
fun MainNavigationBar(
    navController: NavHostController
) {
    NavigationBar(
        modifier = Modifier.semantics { contentDescription = "Bottom Navigation Bar" }
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavBarItems.values.forEach { navBarItem ->
            NavigationBarItem(
                selected = currentRoute == navBarItem.route,
                onClick = {
                    navController.navigate(navBarItem.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == navBarItem.route) {
                            navBarItem.selectedIcon
                        } else navBarItem.unselectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(navBarItem.title),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}