package com.six.sense.presentation.navigation.route

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.graph.SetupSideNavGraph
import com.six.sense.presentation.screen.materialComponents.camera.CameraXViewModel
import com.six.sense.presentation.screen.materialComponents.camera.CaptureScreen
import com.six.sense.utils.composableWithVM
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
        AnimatedContentTransitionScope.SlideDirection.Up
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
    composableWithVM<Screens.Home.CameraX, CameraXViewModel>(navController = navController) {
        val cameraUiState by viewModel.cameraUiState.collectAsStateWithLifecycle()

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(cameraUiState.cameraSelector) {
            viewModel.bindToCamera(
                appContext = context.applicationContext,
                lifecycleOwner = lifecycleOwner
            )
        }
        CaptureScreen(
            cameraUiState = cameraUiState,
            onClickFlipCamera = viewModel::flipCamera,
            onClearQr = viewModel::clearQrData,
            onBack = {
                navController.navigateUp()
            },
            onCaptureImage = {
                viewModel.captureImage(context)
            },
            modifier = modifier
        )
    }
}