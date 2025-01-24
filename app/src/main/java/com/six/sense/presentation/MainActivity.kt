package com.six.sense.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.graph.SetupMainNavGraph
import com.six.sense.ui.theme.SixSenseAndroidTheme
import com.six.sense.utils.collectWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { viewModel.keepSplashOpened }
        super.onCreate(savedInstanceState)
        viewModel.isUiLightMode.collectWithLifecycle {
            enableEdgeToEdge(
                statusBarStyle = if (!it)
                    SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                else
                    SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            )
        }
        enableEdgeToEdge()
        setContent {
            val isUiLightMode by viewModel.isUiLightMode.collectAsStateWithLifecycle()
            SixSenseAndroidTheme(darkTheme = !isUiLightMode) {
                val navController = rememberNavController()
                SetupMainNavGraph(
                    startDestination = if (Firebase.auth.currentUser == null) Screens.Login else Screens.Home,
                    navController = navController
                )
            }
        }
    }
}