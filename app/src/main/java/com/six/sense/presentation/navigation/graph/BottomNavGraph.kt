package com.six.sense.presentation.navigation.graph

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.six.sense.presentation.navigation.MainScreenType
import com.six.sense.presentation.navigation.component.BottomBar
import com.six.sense.presentation.navigation.route.profileRoute

/**
 * A composable function that sets up the bottom navigation graph.
 *
 * @param startDestination The starting destination for the navigation graph.
 * @param currentDestination The name of the currently selected destination.
 * @param onDestinationChanged A callback function that is invoked when the user selects a new destination.
 * @param navController The NavHostController for the main navigation graph.
 * @param navControllerBottomBar The NavHostController for the bottom navigation bar.
 * @param modifier Modifier for the layout.
 */
@Composable
fun SetupBottomNavGraph(
    startDestination: Any,
    currentDestination: String,
    onDestinationChanged: (MainScreenType) -> Unit,
    navController: NavHostController,
    navControllerBottomBar: NavHostController,
    modifier: Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                currentDestination = currentDestination,
                onDestinationChanged = onDestinationChanged
            )
        },
        contentWindowInsets = WindowInsets.navigationBars
    ){ padding->
        val paddingModifier = Modifier.padding(padding)
        NavHost(
            startDestination = startDestination,
            navController = navControllerBottomBar
        ) {
//            chatRoute(
//
//            )
            profileRoute(
                modifier = paddingModifier
            )
        }
    }
}