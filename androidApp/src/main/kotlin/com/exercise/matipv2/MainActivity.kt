package com.exercise.matipv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.exercise.matipv2.platform.CurrentActivity
import com.exercise.matipv2.ui.MainScreen
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.ui.theme.MaTIPv2Theme
import org.koin.android.ext.android.inject
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {

    private val currentActivity: CurrentActivity by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        currentActivity.activity = this

        enableEdgeToEdge()

        setContent {
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
    }

    override fun onDestroy() {
        if (currentActivity.activity === this) currentActivity.activity = null
        super.onDestroy()
    }
}
