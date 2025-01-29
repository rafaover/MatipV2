package com.exercise.matipv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.exercise.matipv2.ui.MainScreen
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.ui.theme.MaTIPv2Theme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            val mainScreenViewModel: MainScreenViewModel = koinViewModel()

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
}
