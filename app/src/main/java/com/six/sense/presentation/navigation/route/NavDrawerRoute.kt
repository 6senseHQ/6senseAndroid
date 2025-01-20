package com.six.sense.presentation.navigation.route

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.chat.ChatView
import com.six.sense.presentation.screen.chat.ChatViewModel
import com.six.sense.presentation.screen.materialComponents.ComponentsScreen
import com.six.sense.presentation.screen.profile.ProfileScreen
import com.six.sense.presentation.screen.profile.ProfileViewModel
import com.six.sense.utils.composableWithVM

/**
 * Defines the navigation route for the authentication flow.
 */
fun NavGraphBuilder.navDrawerRoute(
    modifier: Modifier,
    navController: NavController,
    onLogoutClicked: () -> Unit
) {
    composableWithVM<Screens.Home.Profile, ProfileViewModel>(navController = navController) {
        val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
        ProfileScreen(userInfo = userInfo, modifier = modifier, onLogoutClicked = onLogoutClicked)
    }
    composableWithVM<Screens.Home.Chat, ChatViewModel>(navController = navController) {
        ChatView(modifier = modifier)
    }
    composable<Screens.Home.Components> {
        ComponentsScreen(modifier = modifier)
    }
}