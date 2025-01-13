package com.six.sense.presentation.navigation.route

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.LoginScreen

/**
 * Defines the navigation route for the authentication flow.
 *
 * @param modifier Modifier for the layout.
 */
fun NavGraphBuilder.authRoute(
    modifier: Modifier
) {
    composable<Screens.Login> {
        LoginScreen(
            modifier = modifier
        )
    }
}