package com.exercise.matipv2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.exercise.matipv2.ui.MainScreen
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.ui.theme.MaTIPv2Theme
import org.koin.compose.viewmodel.koinViewModel

/** The whole app as one screen for the iOS app to show. Koin is started before this, in the app delegate. */
fun MainViewController() = ComposeUIViewController {
    val mainScreenViewModel = koinViewModel<MainScreenViewModel>()

    MaTIPv2Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(mainScreenViewModel)
        }
    }
}
