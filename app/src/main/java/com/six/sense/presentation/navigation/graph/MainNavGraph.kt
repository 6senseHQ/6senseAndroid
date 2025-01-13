package com.six.sense.presentation.navigation.graph

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.six.sense.presentation.MainViewModel
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.route
import com.six.sense.presentation.navigation.route.authRoute
import com.six.sense.presentation.navigation.route.homeRoute
import kotlinx.coroutines.flow.mapNotNull

/**
 * A composable function that sets up the main navigation graph.
 *
 * @param startDestination The starting destination for the navigation graph.
 * @param navController The NavHostController for the main navigation graph.
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SetupMainNavGraph(
    startDestination: Any,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<MainViewModel>(context as ViewModelStoreOwner)
    val backStack by navController.currentBackStackEntryFlow.mapNotNull { it.destination.route }
        .collectAsStateWithLifecycle(Screens.serializer().route)
//    val currentBottomNavDestination by viewModel.currentBottomNavDestination.collectAsStateWithLifecycle()
    val currentMainNavDestination = backStack
        .split('.').lastOrNull()
        ?.split('/')?.firstOrNull() ?: ""
    val isOnMainScreen = backStack == Screens.serializer().route
    var mainModifier : Modifier
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

        },
        contentWindowInsets = WindowInsets.statusBars
    ) {
        mainModifier = Modifier.padding(it)
        NavHost(
            startDestination = startDestination,
            navController = navController
        ) {
            authRoute(
                modifier = mainModifier
            )
            homeRoute(
                modifier = mainModifier,
                navController = navController
            )
        }
    }

}