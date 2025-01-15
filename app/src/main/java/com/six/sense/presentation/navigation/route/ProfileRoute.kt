package com.six.sense.presentation.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.profile.ProfileViewModel
import com.six.sense.utils.composableWithVM

/**
 * Defines the navigation route for the authentication flow.
 */
fun NavGraphBuilder.navDrawerRoute(
    navController: NavController
) {
    composableWithVM<Screens.Home.Profile, ProfileViewModel>(navController = navController){
//        ProfileScreen(modifier = modifier)
    }
}