package com.six.sense.domain.repo

import android.graphics.Bitmap
import com.openai.models.Assistant
import com.six.sense.domain.model.MessageData
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Interface defining the contract for interacting with the OpenAI API.
 * This repository provides methods for managing assistants, threads, and messages.
 */
interface OpenAiRepo {
    /**
     * A mutable state flow representing the current list of assistants.
     *
     * This property holds a list of [Assistant] objects, and any updates to this list will be
     * automatically propagated to all collectors of the flow. It is designed to be used
     * for observing changes to the set of available or active assistants within an application.
     *
     * **Key features:**
     *
     * *   **Reactive:** Changes to the list are emitted as new states in the flow, allowing for reactive UI updates.
     * *   **Mutable:** The list of assistants can be modified directly by updating the value of this flow.
     * *   **State-based:** The flow always holds the latest list of assistants, representing the current state.
     * *  **Thread-safe:**  MutableStateFlow is thread safe, allowing updates from different coroutines.
     *
     * **Usage examples:**
     *
     * 1.  **Updating the list:**
     *     ```kotlin
     *     val newAssistants = listOf(Assistant(...), Assistant(...))
     *     assistants.value = newAssistants
     *     ```
     *
     * 2.  **Collecting updates:**
     *     ```kotlin
     *     lifecycleScope.launch {
     *         assistants.collect { currentAssistants ->
     *             // Update UI or perform actions based on the current list of assistants
     *             println("Current assistants: $currentAssistants")
     *         }
     *     }
     *     ```
     *
     * @see Assistant
     * @see MutableStateFlow
     */
    val assistants : MutableStateFlow<List<Assistant>>

    /**
     * A [MutableStateFlow] of active thread IDs (strings).
     *
     * Emits a new list whenever the set of active threads changes.
     * Each string is a unique thread ID. Order is not guaranteed.
     *
     *  You can:
     *  - `collect()` or `collectLatest()` to observe changes.
     *  - `value` to get the current list.
     *  - modify the list with `threads.value = listOf(...)`
     *
     *  Modifying the list via `value` triggers an emission.
     */
    val threads : MutableStateFlow<List<String>>
    suspend fun getAllAssistants(): List<Assistant>
    suspend fun createNewThread(): String
    suspend fun getAllThreads(): List<String>
    suspend fun getThreadMessages(threadId: String): List<MessageData>
    suspend fun sendMessageToThread(assistantId: String, threadId: String, message: String, image: Bitmap?)
}