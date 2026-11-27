package com.exercise.matipv2.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.main_screen_description
import com.exercise.matipv2.generated.resources.password_reset_sent
import com.exercise.matipv2.components.MainNavigationDrawerContent
import com.exercise.matipv2.components.auth.AuthDialog
import com.exercise.matipv2.components.common.MainTopBar
import com.exercise.matipv2.platform.GoogleIdTokenProvider
import com.exercise.matipv2.platform.PlatformActions
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.exercise.matipv2.ui.auth.AuthViewModel
import com.exercise.matipv2.ui.navigation.MatipBottomBar
import com.exercise.matipv2.ui.navigation.MatipNavDisplay
import com.exercise.matipv2.ui.navigation.MatipNavigationRail
import com.exercise.matipv2.ui.navigation.MatipRoute
import com.exercise.matipv2.ui.navigation.NavigationLayout
import com.exercise.matipv2.ui.navigation.currentTopLevelRoute
import com.exercise.matipv2.ui.navigation.navigateToTopLevel
import com.exercise.matipv2.ui.navigation.navigationLayoutFor
import com.exercise.matipv2.ui.navigation.navigationSavedStateConfiguration
import com.exercise.matipv2.ui.navigation.TopLevelDestination
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    authViewModel: AuthViewModel = koinViewModel(),
    platformActions: PlatformActions = koinInject(),
    googleIdTokenProvider: GoogleIdTokenProvider = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val userLists by viewModel.userLists.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(navigationSavedStateConfiguration, MatipRoute.TipCalculator)
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val passwordResetSentMessage = stringResource(Res.string.password_reset_sent)
    val mainScreenDescription = stringResource(Res.string.main_screen_description)

    // Close auth dialog when user signs in
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            authViewModel.updateShowAuthDialog(false)
        }
    }

    if (viewModel.showSnackBar) {
        LaunchedEffect(viewModel.snackBarMessage) {
            snackbarHostState.showSnackbar(viewModel.snackBarMessage)
            viewModel.updateShowSnackBar(false)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainNavigationDrawerContent(
                currentUser = currentUser,
                userLists = userLists,
                isGoogleSignInAvailable = googleIdTokenProvider.isAvailable,
                onSignInGoogle = {
                    authViewModel.signIn { error ->
                        viewModel.updateShowSnackBar(true, error)
                    }
                    scope.launch { drawerState.close() }
                },
                onSignInEmail = {
                    authViewModel.updateShowAuthDialog(true)
                    scope.launch { drawerState.close() }
                },
                onSignOut = {
                    authViewModel.signOut()
                    scope.launch { drawerState.close() }
                },
                onListClick = { list ->
                    backStack.add(MatipRoute.ListTipList(list.id))
                    scope.launch { drawerState.close() }
                },
                onSettingsClick = {
                    if (backStack.lastOrNull() != MatipRoute.Settings) backStack.add(MatipRoute.Settings)
                    scope.launch { drawerState.close() }
                },
                onReviewClick = {
                    platformActions.openStoreListing()
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        BoxWithConstraints {
            val layout = navigationLayoutFor(maxWidth)
            val currentTab = backStack.currentTopLevelRoute()
            val onDestinationSelected = { destination: TopLevelDestination ->
                backStack.navigateToTopLevel(destination.route)
            }

            // The rail sits outside the Scaffold so it runs the full height, behind the system bars.
            Row {
                if (layout == NavigationLayout.Rail) {
                    MatipNavigationRail(currentRoute = currentTab, onDestinationSelected = onDestinationSelected)
                }
                Scaffold(
                    modifier = Modifier
                        .weight(1f)
                        .semantics { contentDescription = mainScreenDescription },
                    topBar = {
                        MainTopBar(
                            onNavigationClick = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = {
                        if (layout == NavigationLayout.BottomBar) {
                            MatipBottomBar(currentRoute = currentTab, onDestinationSelected = onDestinationSelected)
                        }
                    }
                ) { paddingValues ->
                    if (authViewModel.showAuthDialog) {
                        AuthDialog(
                            isLoading = authViewModel.isLoading,
                            errorMessage = authViewModel.authErrorMessage,
                            onDismissRequest = { authViewModel.updateShowAuthDialog(false) },
                            onSignIn = { email, password ->
                                authViewModel.signInWithEmail(email, password)
                            },
                            onSignUp = { email, password ->
                                authViewModel.signUpWithEmail(email, password)
                            },
                            onForgotPassword = { email ->
                                authViewModel.sendPasswordResetEmail(email = email) {
                                    viewModel.updateShowSnackBar(true, passwordResetSentMessage)
                                }
                            },
                            onErrorShown = authViewModel::clearAuthError
                        )
                    }
                    // The bars' space is taken here, so a screen's imePadding only adds the part of the keyboard above them.
                    MatipNavDisplay(
                        backStack = backStack,
                        viewModel = viewModel,
                        uiState = uiState,
                        modifier = Modifier.padding(paddingValues).consumeWindowInsets(paddingValues)
                    )
                }
            }
        }
    }
}
