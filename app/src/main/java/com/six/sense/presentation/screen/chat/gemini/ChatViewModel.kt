package com.six.sense.presentation.screen.chat.gemini

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.six.sense.BuildConfig
import com.six.sense.R
import com.six.sense.domain.repo.GeminiFilesRepo
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
    private val geminiFilesRepo: GeminiFilesRepo,
) : BaseViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()



    private val resourceId = R.raw.login
    val uri = "android.resource://${context.packageName}/$resourceId"

    val inputStream = context.resources.openRawResource(resourceId)


    val fileBytes = inputStream.readBytes()


    val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash-exp",
        apiKey = BuildConfig.apiKey,
        systemInstruction = content {
            text("act as a comedian.")
        }
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

            val response = chat.sendMessage(userPrompt)
            chat.sendMessage(userPrompt)


            _chatUiState.update { it.copy(inputContent = userPrompt) }
            _chatUiState.update { it.copy(outputContent = response.text ?: "") }
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