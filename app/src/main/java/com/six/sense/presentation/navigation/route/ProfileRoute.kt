package com.six.sense.presentation.navigation.route

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.profile.ProfileViewModel
import com.six.sense.utils.composableWithVM

/**
 * Defines the navigation route for the authentication flow.
 *
 * @param modifier Modifier for the layout.
 */
fun NavGraphBuilder.profileRoute(
    modifier: Modifier,
    navController: NavController
) {
    composableWithVM<Screens.Home.Profile, ProfileViewModel>(navController = navController){
//        ProfileScreen(modifier = modifier)
    }
}