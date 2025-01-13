package com.six.sense.presentation.navigation.route

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.auth.LoginScreen
import com.six.sense.presentation.screen.auth.LoginViewModel
import com.six.sense.utils.composableWithVM

/**
 * Defines the navigation route for the authentication flow.
 *
 * @param modifier Modifier for the layout.
 */
fun NavGraphBuilder.authRoute(
    modifier: Modifier,
    navController: NavController
) {
    composableWithVM<Screens.Login, LoginViewModel>(navController = navController){
//        viewModel.login()
        LoginScreen(
            modifier = modifier
        )
    }
}