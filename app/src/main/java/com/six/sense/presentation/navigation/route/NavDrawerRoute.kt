package com.six.sense.presentation.navigation.route

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.six.sense.presentation.EmptyViewModel
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.chat.gemini.ChatView
import com.six.sense.presentation.screen.chat.gemini.ChatViewModel
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
    onLogoutClicked: () -> Unit,
) {
    composableWithVM<Screens.Home.Profile, ProfileViewModel>(navController = navController) {
        val activity = LocalActivity.current
        viewModel.stripePaymentManager.CreatePaymentSheet()
        val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
        ProfileScreen(
            userInfo = userInfo,
            modifier = modifier,
            onLogoutClicked = onLogoutClicked,
            onStripePaymentClicked = {
                viewModel.presentPaymentSheet()
            },
            onPlayInAppPaymentClicked = {
                viewModel.launchPurchaseFlow(activity)
            })

    }
    composableWithVM<Screens.Home.Chat, ChatViewModel>(navController = navController) {
        val chatUiState by viewModel.chatUiState.collectAsStateWithLifecycle()
        ChatView(
            modifier = modifier,
            sendPrompt = { userPrompt, userImage ->
                viewModel.geminiChat(
                    userPrompt = userPrompt,
                    userImage = userImage
                )
            },
            chatUiState = chatUiState
        )
    }
    composableWithVM<Screens.Home.Components, EmptyViewModel>(navController = navController) {
        ComponentsScreen(modifier = modifier)
    }
}