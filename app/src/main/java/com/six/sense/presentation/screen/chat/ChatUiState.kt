package com.six.sense.presentation.screen.chat

import com.openai.models.Assistant
import com.six.sense.presentation.screen.chat.gemini.SystemInstructions

data class ChatUiState(
    val selectedModel: Model = Model.OpenAI,
    val assistantId: String = "",
    val assistants: List<Assistant> = listOf(),
    val outputContent: String = "",
    val inputContent: String = "",
    val chatHistory: List<String> = listOf(),
    val systemRole: SystemInstructions = SystemInstructions.DEFAULT,
)