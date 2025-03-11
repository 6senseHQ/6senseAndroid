package com.six.sense.presentation.navigation.graph

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
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
import com.six.sense.presentation.navigation.route.baseRoute
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
    val mainModifier : Modifier = Modifier.fillMaxSize()
    NavHost (
        startDestination = startDestination,
        navController = navController,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300,
                    delayMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        },
        popExitTransition = {
            fadeOut() + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                targetOffset = { it })
        },

    ) {
        baseRoute(
            navController = navController,
            mainViewModel = viewModel
        )
        authRoute(
            navController = navController,
            modifier = mainModifier
        )
        homeRoute(
            navController = navController,
            modifier = mainModifier
        )
    }

}