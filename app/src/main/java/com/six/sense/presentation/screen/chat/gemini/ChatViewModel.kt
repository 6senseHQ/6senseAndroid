package com.six.sense.presentation.screen.chat.gemini

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.six.sense.BuildConfig
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
) : BaseViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()


    val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash-exp",
        apiKey = BuildConfig.apiKey,
        systemInstruction = content { text(_chatUiState.value.systemRole.instruction) }
    )


    val toneChangerModel = GenerativeModel(
        "gemini-1.5-flash",
        // Retrieve API key as an environmental variable defined in a Build Configuration
        // see https://github.com/google/secrets-gradle-plugin for further instructions
        BuildConfig.apiKey,
        generationConfig = generationConfig {
            temperature = 2f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
    )

    val chatHistory = emptyList<String>()

    val chat = toneChangerModel.startChat()

    // Note that sendMessage() is a suspend function and should be called from
// a coroutine scope or another suspend function

//    val response = chat.sendMessage(content{})

    private val _currentImagePosition = MutableStateFlow<Int?>(null)
    val currentImagePosition = _currentImagePosition.asStateFlow()

    fun geminiChat(userPrompt: String, userImage: Bitmap) {
        launch {
            val chat = generativeModel.startChat()
            val response = chat.sendMessage(
                content {
                    text(userPrompt)
                    image(userImage)
                }
            )

            _chatUiState.update {
                it.copy(inputContent = userPrompt, outputContent = response.text ?: "",
                    chatHistory = it.chatHistory.apply {
                        add(userPrompt)
                        add(response.text ?: "")
                    })
            }
        }
    }

    fun changeSystemRole(systemRole: SystemInstructions) {
        _chatUiState.update { it.copy(systemRole = systemRole) }
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
//            val fileLink = geminiFilesRepo.uploadFile()
            val response = generativeModel
                .generateContent(content {
                    text(prompt)
//                    fileData(fileLink , "text/plain")
                })
            response.text?.let { outputContent ->
                _chatUiState.update { it.copy(outputContent = outputContent) }
            }
        }
    }
}