package com.six.sense.presentation.screen.chat.gemini

import androidx.lifecycle.SavedStateHandle
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.six.sense.BuildConfig
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel for the chat screen.
 *
 * This ViewModel manages the state and logic for the chat screen.
 *
 * @property savedStateHandle SavedStateHandle for managing state across configuration changes.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()

    val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash-exp",
        apiKey = BuildConfig.apiKey
    )

    val geminiChat = generativeModel.startChat(
        history = listOf(
            content(role = "user") {
                "Hi there! How can I assist you today?"
            },
            content(role = "model") {
                "Hello! I'm Gemini, your friendly virtual assistant. How can I help you today?"
            }
        )
    )

    fun geminiChat(
        userPrompt: String,
        modelResponse: String,
    ) {
        launch {
            val chat = generativeModel.startChat(
                history = listOf(
                    content(role = "user") {
                        text(userPrompt)
                    },
                    content(role = "model") {
                        text(modelResponse)
                    }
                )
            )

            chat.sendMessage(userPrompt)
        }
    }

    /**
     * Send prompt.
     *
     * @param prompt Prompt to send.
     */
    fun sendPrompt(
        prompt: String,
    ) {
        launch {
            val response = generativeModel.generateContent(prompt)
            response.text?.let { outputContent ->
                _chatUiState.update { it.copy(outputContent = outputContent) }
            }
        }
    }
}