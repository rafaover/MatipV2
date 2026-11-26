package com.exercise.matipv2.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.ui.lists.ListTipListScreen
import com.exercise.matipv2.ui.lists.ListsScreen
import com.exercise.matipv2.ui.settings.SettingsScreen
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreen
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreenUiState

/** Tabs start slightly smaller than their final size, so they grow into place. */
private const val TAB_ENTER_INITIAL_SCALE = 0.92f

@Composable
fun NavigationGraph(
    viewModel: MainScreenViewModel,
    navController: NavHostController,
    uiState: TipCalculatorScreenUiState
) {
    val motionScheme = MaterialTheme.motionScheme
    val fadeSpec = motionScheme.defaultEffectsSpec<Float>()
    val scaleSpec = motionScheme.defaultSpatialSpec<Float>()
    val slideSpec = motionScheme.defaultSpatialSpec<IntOffset>()

    NavHost(
        navController = navController,
        startDestination = NavBarItems.TipCalculator.route,
        enterTransition = {
            fadeIn(animationSpec = fadeSpec) +
                    scaleIn(initialScale = TAB_ENTER_INITIAL_SCALE, animationSpec = scaleSpec)
        },
        exitTransition = {
            fadeOut(animationSpec = fadeSpec)
        },
        popEnterTransition = {
            fadeIn(animationSpec = fadeSpec) +
                    scaleIn(initialScale = TAB_ENTER_INITIAL_SCALE, animationSpec = scaleSpec)
        },
        popExitTransition = {
            fadeOut(animationSpec = fadeSpec)
        }
    ) {
        composable(NavBarItems.TipCalculator.route) {
            TipCalculatorScreen(
                viewModel = viewModel,
                uiState = uiState
            )
        }
        composable(NavBarItems.Lists.route) {
            viewModel.updateShowSnackBar(false)
            ListsScreen(
                viewModel = viewModel,
                navigateTo = { list ->
                    navController.navigate("ListTipList/${list.id}")
                }
            )
        }
        composable(
            route = "settings",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = slideSpec
                ) + fadeIn(animationSpec = fadeSpec)
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = slideSpec
                ) + fadeOut(animationSpec = fadeSpec)
            }
        ) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onShowMessage = { message ->
                    viewModel.updateShowSnackBar(true, message)
                }
            )
        }
        dialog(
            route = "ListTipList/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val listId = navBackStackEntry.arguments?.getInt("listId")
                ?: error("listId parameter wasn't found.")

            ListTipListScreen(
                viewModel = viewModel,
                listId = listId,
                onDismissRequest = { navController.navigateUp() },
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}
