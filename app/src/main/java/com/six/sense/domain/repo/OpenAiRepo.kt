package com.six.sense.domain.repo

import com.openai.models.Assistant
import com.openai.models.Message
import kotlinx.coroutines.flow.MutableStateFlow

interface OpenAiRepo {
    val assistants : MutableStateFlow<List<Assistant>>
    val threads : MutableStateFlow<List<String>>
    suspend fun getAllAssistants(): List<Assistant>
    suspend fun createNewThread(): String
    suspend fun getAllThreads(): List<String>
    suspend fun getThreadMessages(threadId: String): List<Message>
    suspend fun sendMessageToThread(assistantId: String, threadId: String, message: String)
}