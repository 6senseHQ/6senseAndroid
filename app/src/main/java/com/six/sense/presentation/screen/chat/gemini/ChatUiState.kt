package com.six.sense.presentation.screen.chat.gemini

data class ChatUiState(
    val outputContent: String = "",
    val inputContent: String = "",
    val chatHistory: MutableList<String> = mutableListOf<String>(),
    val systemRole: SystemInstructions = SystemInstructions.DEFAULT,
)