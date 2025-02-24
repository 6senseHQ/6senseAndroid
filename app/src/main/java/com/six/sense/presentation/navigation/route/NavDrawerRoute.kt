package com.six.sense.presentation.navigation.route

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.six.sense.presentation.EmptyViewModel
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.screen.chat.ChatScreen
import com.six.sense.presentation.screen.chat.ChatViewModel
import com.six.sense.presentation.screen.chat.Model
import com.six.sense.presentation.screen.materialComponents.ComponentsScreen
import com.six.sense.presentation.screen.profile.ProfileScreen
import com.six.sense.presentation.screen.profile.ProfileViewModel
import com.six.sense.utils.composableWithVM
import kotlinx.coroutines.flow.update

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
        val showModelDialog by mainViewModel.showChatDialog.collectAsStateWithLifecycle()
        val selectedModel = remember { mutableStateOf(Model.OpenAI) }

        LaunchedEffect(selectedModel.value) {
            viewModel.chatUiState.update {
                it.copy(chatHistory = emptyList())
            }
            when (selectedModel.value) {
                Model.Gemini -> Unit
                Model.OpenAI -> {
                    viewModel.initOpenAi()
                }
            }
        }

        ChatScreen(
            modifier = modifier,
            selectedModel = selectedModel,
            showModelDialog = showModelDialog,
            sendPrompt = { userPrompt, userImage ->
                when (selectedModel.value) {
                    Model.Gemini -> viewModel.geminiChat(
                        userPrompt = userPrompt,
                        userImage = userImage
                    )
                    Model.OpenAI -> viewModel.openAiChat(
                        userPrompt = userPrompt,
                        userImage = userImage
                    )
                }
            },
            chatUiState = chatUiState,
            onDismissRequest = { mainViewModel.showChatDialog.value = false },
            onClickAssistant = { id ->
                viewModel.chatUiState.update {
                    it.copy(assistantId = id)
                }
            },
            navigateToImageViewer = {
                mainViewModel.bitmap = it
                navController.navigate(Screens.ImageViewer)
            }
        )
    }
    composableWithVM<Screens.Home.Components, EmptyViewModel>(navController = navController) {
        ComponentsScreen(
            modifier = modifier,
            onNavigateToCamera = {
                navController.navigate(Screens.Home.CameraX)
            })
    }
}