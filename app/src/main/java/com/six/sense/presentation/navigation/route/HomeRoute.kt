package com.six.sense.presentation.navigation.route

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.six.sense.presentation.MainViewModel
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.graph.SetupBottomNavGraph
import kotlinx.coroutines.flow.mapNotNull

/**
 * Defines the navigation route for the authentication flow.
 *
 * @param modifier Modifier for the layout.
 */
fun NavGraphBuilder.homeRoute(
    modifier: Modifier,
    navController: NavHostController
) {
    composable<Screens.Home> {
        val context = LocalContext.current
        val navControllerBottomBar = rememberNavController()
        val viewModel = hiltViewModel<MainViewModel>(context as ViewModelStoreOwner)

        val currentDestination by navControllerBottomBar.currentBackStackEntryFlow.mapNotNull { it.destination.route?.split(".")?.lastOrNull() }
            .collectAsStateWithLifecycle("")

        SetupBottomNavGraph(
            startDestination = Screens.Home,
            currentDestination = currentDestination,
            onDestinationChanged  = {destination->
                navControllerBottomBar.navigate(destination.route){
                    popUpTo(Screens.Home)
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