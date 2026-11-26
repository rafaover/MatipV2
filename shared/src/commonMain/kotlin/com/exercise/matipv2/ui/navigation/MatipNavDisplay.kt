package com.exercise.matipv2.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.ui.lists.ListTipListScreen
import com.exercise.matipv2.ui.lists.ListsScreen
import com.exercise.matipv2.ui.settings.SettingsScreen
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreen
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreenUiState

/** Tabs start slightly smaller than their final size, so they grow into place. */
private const val TAB_ENTER_INITIAL_SCALE = 0.92f

/** Shows the screen on top of [backStack]. Motion comes from the theme's expressive motion scheme. */
@Composable
fun MatipNavDisplay(
    backStack: MutableList<NavKey>,
    viewModel: MainScreenViewModel,
    uiState: TipCalculatorScreenUiState,
    modifier: Modifier = Modifier,
) {
    val motionScheme = MaterialTheme.motionScheme
    val fadeSpec = motionScheme.defaultEffectsSpec<Float>()
    val scaleSpec = motionScheme.defaultSpatialSpec<Float>()
    val slideSpec = motionScheme.defaultSpatialSpec<IntOffset>()
    val dialogSceneStrategy = remember { DialogSceneStrategy<NavKey>() }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        sceneStrategies = listOf(dialogSceneStrategy),
        transitionSpec = {
            (fadeIn(fadeSpec) + scaleIn(initialScale = TAB_ENTER_INITIAL_SCALE, animationSpec = scaleSpec)) togetherWith
                fadeOut(fadeSpec)
        },
        popTransitionSpec = {
            (fadeIn(fadeSpec) + scaleIn(initialScale = TAB_ENTER_INITIAL_SCALE, animationSpec = scaleSpec)) togetherWith
                fadeOut(fadeSpec)
        },
        predictivePopTransitionSpec = {
            (fadeIn(fadeSpec) + scaleIn(initialScale = TAB_ENTER_INITIAL_SCALE, animationSpec = scaleSpec)) togetherWith
                fadeOut(fadeSpec)
        },
        entryProvider = entryProvider<NavKey> {
            entry<MatipRoute.TipCalculator> {
                TipCalculatorScreen(viewModel = viewModel, uiState = uiState)
            }
            entry<MatipRoute.Lists> {
                LaunchedEffect(Unit) { viewModel.updateShowSnackBar(false) }
                ListsScreen(
                    viewModel = viewModel,
                    navigateTo = { list -> backStack.add(MatipRoute.ListTipList(list.id)) }
                )
            }
            entry<MatipRoute.Settings>(
                metadata = NavDisplay.transitionSpec {
                    (slideInHorizontally(slideSpec) { fullWidth -> fullWidth } + fadeIn(fadeSpec)) togetherWith
                        fadeOut(fadeSpec)
                } + NavDisplay.popTransitionSpec {
                    fadeIn(fadeSpec) togetherWith
                        (slideOutHorizontally(slideSpec) { fullWidth -> fullWidth } + fadeOut(fadeSpec))
                }
            ) {
                SettingsScreen(
                    onBackClick = { backStack.removeLastOrNull() },
                    onShowMessage = { message -> viewModel.updateShowSnackBar(true, message) }
                )
            }
            entry<MatipRoute.ListTipList>(
                metadata = DialogSceneStrategy.dialog(fullScreenDialogProperties())
            ) { route ->
                ListTipListScreen(
                    viewModel = viewModel,
                    listId = route.listId,
                    onBackClick = { backStack.removeLastOrNull() }
                )
            }
        },
    )
}
