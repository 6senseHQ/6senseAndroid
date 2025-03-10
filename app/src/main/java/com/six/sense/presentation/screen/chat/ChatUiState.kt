package com.six.sense.presentation.screen.chat

import com.openai.models.Assistant
import com.six.sense.domain.model.MessageData
import com.six.sense.presentation.screen.chat.gemini.SystemInstructions

/**
 * Represents the UI state for the chat screen.
 *
 * This data class holds all the necessary information to render the chat UI,
 * including the selected language model, the current assistant, the list of
 * available assistants, the content of the input and output areas, the chat
 * history, and the system instructions.
 *
 * @property selectedModel The currently selected language model. Defaults to [Model.OpenAI].
 * @property assistantId The ID of the currently selected assistant. Defaults to an empty string.
 * @property assistants The list of available assistants. Defaults to an empty list.
 * @property outputContent The content currently displayed in the output area (e.g., the assistant's response). Defaults to an empty string.
 * @property inputContent The content currently in the input area (e.g., the user's message). Defaults to an empty string.
 * @property chatHistory The list of messages that make up the chat history. Defaults to an empty list.
 * @property systemRole The system instructions that are guiding the conversation. Defaults to [SystemInstructions.DEFAULT].
 *
 * This class is immutable.
 */
data class ChatUiState(
    /**
     * The currently selected language model.
     */
    val selectedModel: Model = Model.OpenAI,
    /**
     * The ID of the currently selected assistant.
     */
    val assistantId: String = "",
    /**
     * The list of available assistants.
     */
    val assistants: List<Assistant> = listOf(),
    /**
     * The content currently displayed in the output area (e.g., the assistant's response).
     */
    val outputContent: String = "",
    /**
     * The content currently in the input area (e.g., the user's message).
     */
    val inputContent: String = "",
    /**
     * The list of messages that make up the chat history.
     */
    val chatHistory: List<MessageData> = listOf(),
    /**
     * The system instructions that are guiding the conversation.
     */
    val systemRole: SystemInstructions = SystemInstructions.DEFAULT,
)