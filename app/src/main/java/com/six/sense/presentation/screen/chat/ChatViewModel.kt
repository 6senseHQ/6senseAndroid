package com.six.sense.presentation.screen.chat

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.content
import com.six.sense.BuildConfig
import com.six.sense.domain.model.MessageData
import com.six.sense.domain.repo.OpenAiRepo
import com.six.sense.presentation.base.BaseViewModel
import com.six.sense.presentation.screen.chat.gemini.SystemInstructions
import com.six.sense.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val openAiRepo: OpenAiRepo,
) : BaseViewModel() {
    val chatUiState = MutableStateFlow(ChatUiState())


    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash-exp",
        apiKey = BuildConfig.apiKey,
        systemInstruction = content { text(chatUiState.value.systemRole.instruction) }
    )

    // Note that sendMessage() is a suspend function and should be called from
// a coroutine scope or another suspend function

//    val response = chat.sendMessage(content{})

    fun geminiChat(userPrompt: String, userImage: Bitmap?) {
        launch {
            val chat = generativeModel.startChat()
            val time = System.currentTimeMillis()
            val response = chat.sendMessage(
                content {
                    parts.addAll(chatUiState.value.chatHistory.map { TextPart(it.message) })
                    text(userPrompt)
                    userImage?.let { image(it) }
                }
            )

            chatUiState.update {
                it.copy(
                    inputContent = userPrompt, outputContent = response.text ?: "",
                    chatHistory = it.chatHistory.toMutableList().apply {
                        add(MessageData(
                            message = userPrompt,
                            image = userImage,
                            time = time,
                            role = Role.USER
                        ))
                        add(MessageData(
                            message = response.text.orEmpty(),
                            image = null,
                            time = System.currentTimeMillis(),
                            role = Role.ASSISTANT
                        ))
                    }.toList()
                )
            }
        }
    }

    fun changeSystemRole(systemRole: SystemInstructions) {
        chatUiState.update { it.copy(systemRole = systemRole) }
    }

    fun initOpenAi() {
        launch {
            val threadIds = openAiRepo.getAllThreads()
            val ass = openAiRepo.getAllAssistants()
            ass.log()
            ass.firstOrNull()?.id()?.let { id ->
                chatUiState.update {
                    it.copy(assistantId = id, assistants = ass)
                }
            }
            if (threadIds.isEmpty()) return@launch
            getOpenAiThreadHistory(threadIds.first())
        }
    }

    fun openAiChat(userPrompt: String, userImage: Bitmap?){
        launch {
            (openAiRepo.threads.value.lastOrNull() ?: openAiRepo.createNewThread()).let {
                it.log("threadId")
                openAiRepo.sendMessageToThread(
                    assistantId = chatUiState.value.assistantId,
                    threadId = it,
                    message = userPrompt,
                    image = userImage
                )
                if(userPrompt.isNotEmpty())
                    getOpenAiThreadHistory(it)
                else chatUiState.update { state ->
                    state.copy(chatHistory = emptyList())
                }
            }
        }
    }

    private suspend fun getOpenAiThreadHistory(threadId: String) {
        openAiRepo.getThreadMessages(threadId).also {
            it.log()
        }.let {
            chatUiState.update { state ->
                state.copy(chatHistory = it)
            }
        }
    }
}