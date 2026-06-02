package com.exercise.matipv2.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.exercise.matipv2.R
import com.exercise.matipv2.components.MainNavigationBar
import com.exercise.matipv2.components.common.MainTopBar
import com.exercise.matipv2.ui.navigation.NavigationGraph
import kotlinx.coroutines.launch

@SuppressLint("VisibleForTests")
@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                if (currentUser == null) {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                        label = { Text(text = stringResource(R.string.sign_in)) },
                        selected = false,
                        onClick = {
                            viewModel.signIn(context)
                            scope.launch { drawerState.close() }
                        }
                    )
                } else {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                        label = {
                            Column {
                                Text(text = currentUser?.name ?: stringResource(R.string.sign_in))
                                currentUser?.email?.let {
                                    Text(
                                        text = it,
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        },
                        selected = false,
                        onClick = {
                            // User Profile logic could go here
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                        label = { Text(text = stringResource(R.string.sign_out)) },
                        selected = false,
                        onClick = {
                            viewModel.signOut()
                            scope.launch { drawerState.close() }
                        }
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Feedback, contentDescription = null) },
                    label = { Text(text = stringResource(R.string.feedback)) },
                    selected = false,
                    onClick = {
                        // TODO: Implement feedback logic
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Description, contentDescription = null) },
                    label = { Text(text = stringResource(R.string.terms_and_conditions)) },
                    selected = false,
                    onClick = {
                        // TODO: Implement T&C logic
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text(text = stringResource(R.string.settings)) },
                    selected = false,
                    onClick = {
                        // TODO: Implement settings logic
                        scope.launch { drawerState.close() }
                    }
                )
            }
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
