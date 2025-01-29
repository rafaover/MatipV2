package com.exercise.matipv2.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.rememberNavController
import com.exercise.matipv2.components.MainNavigationBar
import com.exercise.matipv2.components.common.MainTopBar
import com.exercise.matipv2.ui.navigation.NavigationGraph

@SuppressLint("VisibleForTests")
@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.semantics {
            contentDescription = "Main Screen with a TopBar, Tip Calculator Screen and " +
                    "bottom Navigation Bar"
        },
        topBar = { MainTopBar() },
        bottomBar = { MainNavigationBar(navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

    ) {
        Column(modifier = Modifier.padding(it)) {
            NavigationGraph(
                viewModel = viewModel,
                navController = navController,
                uiState = uiState,
                snackbarHostState = snackbarHostState
            )
        }
    }
}