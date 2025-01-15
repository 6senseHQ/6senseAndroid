package com.six.sense.presentation.navigation.route

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.chat.ChatView
import com.six.sense.presentation.screen.chat.ChatViewModel
import com.six.sense.presentation.screen.profile.ProfileViewModel
import com.six.sense.utils.composableWithVM

/**
 * Defines the navigation route for the authentication flow.
 */
fun NavGraphBuilder.navDrawerRoute(
    modifier : Modifier,
    navController: NavController
) {
    composableWithVM<Screens.Home.Profile, ProfileViewModel>(navController = navController){
//        ProfileScreen(modifier = modifier)
    }
    composableWithVM<Screens.Home.Chat, ChatViewModel>(navController = navController){
        ChatView(modifier = modifier)
    }
}