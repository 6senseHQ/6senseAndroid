package com.six.sense.presentation.navigation.route

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.graph.SetupSideNavGraph
import kotlinx.coroutines.flow.mapNotNull

/**
 * Defines the navigation route for the authentication flow.
 *
 * @param modifier Modifier for the layout.
 */
fun NavGraphBuilder.homeRoute(
    navController: NavHostController,
    modifier: Modifier
) {
    composable<Screens.Home> {
        val navControllerBottomBar = rememberNavController()
        val currentDestination by navControllerBottomBar.currentBackStackEntryFlow.mapNotNull { it.destination.route?.split(".")?.lastOrNull() }
            .collectAsStateWithLifecycle("")

        SetupSideNavGraph(
            startDestination = Screens.Home.Chat,
            currentDestination = currentDestination,
            onDestinationChanged  = {destination->
                navControllerBottomBar.navigate(destination.route){
                    popUpTo(Screens.Home.Chat)
                    launchSingleTop = true
                }
//                viewModel.changeBottomNavDestination(destination.name)
            },
            navController = navController,
            navControllerBottomBar =navControllerBottomBar,
            modifier=modifier
        )
    }
}