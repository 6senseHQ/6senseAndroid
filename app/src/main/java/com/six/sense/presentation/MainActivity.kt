package com.six.sense.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.graph.SetupMainNavGraph
import com.six.sense.ui.theme.SixSenseAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel by viewModels<MainViewModel>()
        setContent {
            val isUiLightMode by viewModel.isUiLightMode.collectAsStateWithLifecycle()
            SixSenseAndroidTheme(darkTheme = !isUiLightMode) {
                val navController = rememberNavController()
                SetupMainNavGraph(
                    startDestination = if (Firebase.auth.currentUser != null) Screens.Login else Screens.Home,
                    navController = navController
                )
            }
        }
    }
}