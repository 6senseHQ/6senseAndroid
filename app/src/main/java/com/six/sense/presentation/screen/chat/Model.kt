package com.six.sense.presentation.screen.chat

enum class Model {
    OpenAI,
    Gemini
}
enum class Role{
    ASSISTANT,
    USER
}

val String.asRole get() = Role.entries.find { it.name == this } ?: Role.USER