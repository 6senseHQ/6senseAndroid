package com.six.sense.domain.repo

import android.graphics.Bitmap
import com.openai.models.Assistant
import com.six.sense.domain.model.MessageData
import kotlinx.coroutines.flow.MutableStateFlow

interface OpenAiRepo {
    val assistants : MutableStateFlow<List<Assistant>>
    val threads : MutableStateFlow<List<String>>
    suspend fun getAllAssistants(): List<Assistant>
    suspend fun createNewThread(): String
    suspend fun getAllThreads(): List<String>
    suspend fun getThreadMessages(threadId: String): List<MessageData>
    suspend fun sendMessageToThread(assistantId: String, threadId: String, message: String, image: Bitmap?)
}