package com.exercise.matipv2.ui.navigation

import androidx.compose.material3.SnackbarHostState
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
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreen
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreenUiState
import org.koin.compose.koinInject

@Composable
fun NavigationGraph(
    viewModel: MainScreenViewModel,
    navController: NavHostController,
    uiState: TipCalculatorScreenUiState,
    snackbarHostState: SnackbarHostState,
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
        startDestination = NavBarItems.TipCalculator.route
    ) {
        composable(NavBarItems.TipCalculator.route) {
            TipCalculatorScreen(
                viewModel = viewModel,
                uiState = uiState,
                snackbarHostState = snackbarHostState
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