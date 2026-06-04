package com.exercise.matipv2.ui.navigation

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.exercise.matipv2.data.analytics.AnalyticsHelper
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.ui.lists.ListTipListScreen
import com.exercise.matipv2.ui.lists.ListsScreen
import com.exercise.matipv2.ui.settings.SettingsScreen
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreen
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreenUiState
import org.koin.compose.koinInject

@Composable
fun NavigationGraph(
    viewModel: MainScreenViewModel,
    navController: NavHostController,
    uiState: TipCalculatorScreenUiState,
    analyticsHelper: AnalyticsHelper = koinInject()
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry.value) {
        navBackStackEntry.value?.destination?.route?.let { route ->
            analyticsHelper.logScreenView(screenName = route)
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavBarItems.TipCalculator.route,
        enterTransition = {
            fadeIn(animationSpec = tween(300, easing = LinearEasing)) + 
                    scaleIn(initialScale = 0.92f, animationSpec = tween(300, easing = EaseOut))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(150, easing = LinearEasing))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300, easing = LinearEasing)) + 
                    scaleIn(initialScale = 0.92f, animationSpec = tween(300, easing = EaseOut))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(150, easing = LinearEasing))
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
                    animationSpec = tween(300, easing = EaseOut)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, 
                    animationSpec = tween(300, easing = EaseIn)
                ) + fadeOut(animationSpec = tween(300))
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
