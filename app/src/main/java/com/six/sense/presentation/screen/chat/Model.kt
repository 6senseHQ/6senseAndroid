package com.six.sense.presentation.screen.chat

import com.six.sense.presentation.screen.chat.Role.ASSISTANT
import com.six.sense.presentation.screen.chat.Role.USER


/**
 * @brief Enumerates the available language models.
 *
 * This enum class represents the different language models that can be used
 * by the system. Each enumerator corresponds to a specific model, and they
 * can be used to select the desired model for a particular operation.
 */
enum class Model {
    OpenAI,
    Gemini
}
/**
 * Represents the possible roles a participant can have in a conversation or interaction.
 *
 * This enum defines the different roles that can be assigned to entities
 * involved in a system, such as an AI assistant or a human user.
 *
 * @property ASSISTANT Represents the role of an AI assistant, providing responses and support.
 * @property USER Represents the role of a human user, initiating requests and interacting with the system.
 */
enum class Role{
    ASSISTANT,
    USER
}

/**
 * Converts a string to a [Role] enum value.
 *
 * This property searches for a matching [Role] enum entry based on the string's name.
 * If a match is found, the corresponding [Role] is returned.
 * If no match is found, the default [Role.USER] is returned.
 *
 * @return The [Role] enum value corresponding to the string, or [Role.USER] if no match is found.
 * @see Role
 */
val String.asRole get() = Role.entries.find { it.name == this } ?: Role.USER