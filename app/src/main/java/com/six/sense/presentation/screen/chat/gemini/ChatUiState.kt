package com.six.sense.presentation.screen.chat.gemini

data class ChatUiState(
    val outputContent: String = "",
    val inputContent: String = "",
    val chatHistory: List<String> = emptyList(),
)