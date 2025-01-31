package com.six.sense.presentation.navigation.route

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.six.sense.presentation.MainViewModel
import com.six.sense.presentation.base.ImageViewerDialog
import com.six.sense.presentation.navigation.Screens

/**
 * Defines the navigation route for the base flow.
 *
 */
fun NavGraphBuilder.baseRoute(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    dialog<Screens.ErrorDialog> {
        AlertDialog(
            title = {
                Text(text = "Warning")
            },
            text = {
                Text(text = it.toRoute<Screens.ErrorDialog>().error)
            },
            onDismissRequest = navController::popBackStack,
            confirmButton = {
                Button(onClick = navController::popBackStack) {
                    Text(text = "Close")
                }
            }
        )
    }
    dialog<Screens.ImageViewer>(dialogProperties = DialogProperties(decorFitsSystemWindows= false, usePlatformDefaultWidth= false)){
        mainViewModel.bitmap?.let { bitmap ->
            ImageViewerDialog(bitmap, onDismissRequest = navController::popBackStack)
        }
    }
}