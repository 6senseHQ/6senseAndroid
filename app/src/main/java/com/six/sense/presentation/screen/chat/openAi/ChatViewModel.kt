package com.six.sense.presentation.screen.chat.openAi

import androidx.lifecycle.SavedStateHandle
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : BaseViewModel() {

    /**
     * Ui state.
     *
     * @property initial Initial state
     * @property isLoading Loading state
     * @property error Error state
     * @property outputContent Output content state
     * @constructor Create empty Ui state
     */
    data class UiState(
        /**
         * Initial state.
         */
        val initial: Boolean = true,
        /**
         * Loading state.
         */
        val isLoading: Boolean = false,
        /**
         * Error state.
         */
        val error: String? = null,
        /**
         * Output content state.
         */
        val outputContent: String? = null,
    )

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState().copy(initial = true))

    /**
     * Ui state.
     */
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()



    /**
     * Send prompt.
     *
     * @param prompt Prompt to send.
     */
    fun sendPrompt(
        prompt: String,
    ) {
        _uiState.value = UiState().copy(isLoading = true)
    }
}