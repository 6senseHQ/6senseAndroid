package com.six.sense.presentation.navigation.route

import android.app.Activity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    navController: NavController,
    modifier: Modifier = Modifier
) {
    composableWithVM<Screens.Login, LoginViewModel>(navController = navController) {
        val activity = LocalContext.current as Activity
        val onSuccess = {
            navController.navigate(Screens.Home)
        }
        LoginScreen(
            onClickFacebookLogin = {
                viewModel.facebookSignIn(button = it, onSuccess = onSuccess)
            },
            onClickGoogleLogin = {
                viewModel.googleSignIn(activity = activity, onSuccess = onSuccess)
            },
            modifier = modifier
        )
    }
}