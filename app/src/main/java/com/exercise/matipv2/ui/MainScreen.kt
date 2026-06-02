package com.exercise.matipv2.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.rememberNavController
import com.exercise.matipv2.R
import com.exercise.matipv2.components.MainNavigationBar
import com.exercise.matipv2.components.MainNavigationDrawerContent
import com.exercise.matipv2.components.auth.AuthDialog
import com.exercise.matipv2.components.common.MainTopBar
import com.exercise.matipv2.ui.auth.AuthViewModel
import com.exercise.matipv2.ui.navigation.NavigationGraph
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("VisibleForTests")
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    authViewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val passwordResetSentMessage = stringResource(R.string.password_reset_sent)

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
                onSignInGoogle = {
                    authViewModel.signIn(context) { error ->
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
                onFeedbackClick = {
                    // TODO: Implement feedback logic
                    scope.launch { drawerState.close() }
                },
                onTermsClick = {
                    // TODO: Implement T&C logic
                    scope.launch { drawerState.close() }
                },
                onSettingsClick = {
                    // TODO: Implement settings logic
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.semantics {
                contentDescription = "Main Screen with a TopBar, Tip Calculator Screen and " +
                        "bottom Navigation Bar"
            },
            topBar = {
                MainTopBar(
                    onNavigationClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            bottomBar = { MainNavigationBar(navController = navController) },
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
                    uiState = uiState,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
