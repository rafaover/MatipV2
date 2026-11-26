package com.exercise.matipv2.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.main_screen_description
import com.exercise.matipv2.generated.resources.password_reset_sent
import com.exercise.matipv2.components.MainNavigationDrawerContent
import com.exercise.matipv2.components.auth.AuthDialog
import com.exercise.matipv2.components.common.MainTopBar
import com.exercise.matipv2.platform.PlatformActions
import com.exercise.matipv2.ui.auth.AuthViewModel
import com.exercise.matipv2.ui.navigation.NavBarItems
import com.exercise.matipv2.ui.navigation.NavigationGraph
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    authViewModel: AuthViewModel = koinViewModel(),
    platformActions: PlatformActions = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val userLists by viewModel.getAllLists().collectAsState(initial = emptyList())
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val passwordResetSentMessage = stringResource(Res.string.password_reset_sent)
    val mainScreenDescription = stringResource(Res.string.main_screen_description)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

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
                    navController.navigate("ListTipList/${list.id}")
                    scope.launch { drawerState.close() }
                },
                onSettingsClick = {
                    navController.navigate("settings")
                    scope.launch { drawerState.close() }
                },
                onReviewClick = {
                    platformActions.openStoreListing()
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                NavBarItems.values.forEach { item ->
                    item(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    if (currentDestination?.hierarchy?.any { it.route == item.route } == true) {
                                        item.selectedIcon
                                    } else item.unselectedIcon
                                ),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(item.title),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        ) {
            Scaffold(
                modifier = Modifier.semantics {
                    contentDescription = mainScreenDescription
                },
                topBar = {
                    MainTopBar(
                        onNavigationClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) { paddingValues ->
                if (authViewModel.showAuthDialog) {
                    AuthDialog(
                        isLoading = authViewModel.isLoading,
                        onDismissRequest = { authViewModel.updateShowAuthDialog(false) },
                        onSignIn = { email, password ->
                            authViewModel.signInWithEmail(email, password) { error ->
                                viewModel.updateShowSnackBar(true, error)
                            }
                        },
                        onSignUp = { email, password ->
                            authViewModel.signUpWithEmail(email, password) { error ->
                                viewModel.updateShowSnackBar(true, error)
                            }
                        },
                        onForgotPassword = { email ->
                            authViewModel.sendPasswordResetEmail(
                                email = email,
                                onSuccess = {
                                    viewModel.updateShowSnackBar(true, passwordResetSentMessage)
                                },
                                onError = { error ->
                                    viewModel.updateShowSnackBar(true, error)
                                }
                            )
                        }
                    )
                }
                Column(modifier = Modifier.padding(paddingValues)) {
                    NavigationGraph(
                        viewModel = viewModel,
                        navController = navController,
                        uiState = uiState
                    )
                }
            }
        }
    }
}
