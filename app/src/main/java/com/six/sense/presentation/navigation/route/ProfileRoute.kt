package com.six.sense.presentation.navigation.route

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.six.sense.presentation.navigation.Screens

/**
 * Defines the navigation route for the authentication flow.
 *
 * @param modifier Modifier for the layout.
 */
fun NavGraphBuilder.profileRoute(
    modifier: Modifier,
) {
    composable<Screens.Home.Profile> {
//        ProfileScreen(modifier = modifier)
    }
}