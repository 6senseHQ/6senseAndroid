package com.six.sense.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Abstract base class for ViewModels.
 *
 * Provides common functionality for ViewModels, such as error handling,
 * loading state management, and navigation.
 */
abstract class BaseViewModel: ViewModel() {

    /**
     * SharedFlow for default error messages.
     */
    val errorFlow= MutableSharedFlow<String>()
    /**
     * SharedFlow for success messages.
     */
    val successFlow= MutableSharedFlow<String>()
    /**
     * SharedFlow for loading state.
     */
    val loadingFlow = MutableStateFlow(false)


    protected val mainContext: CoroutineContext = Dispatchers.Main
    protected val ioContext: CoroutineContext = Dispatchers.IO

    protected open var defaultErrorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        sendError(throwable)
    }

    /**
     * Launches a coroutine with the specified dispatcher and error handling.
     *
     * @param dispatcher The coroutine dispatcher to use.
     * @param showLoading Whether to emit loading state.
     * @param coroutineExceptionHandler The coroutine exception handler.
     * @param block The coroutine block to execute.
     * @return The launched coroutine [Job].
     */
    protected fun launch(
        dispatcher: CoroutineContext = mainContext,
        showLoading: Boolean = true,
        coroutineExceptionHandler: CoroutineExceptionHandler = defaultErrorHandler,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            try {
                if(showLoading)
                    loadingFlow.value = true
                block()
            } finally {
                if(showLoading)
                    loadingFlow.value = false
            }
        }
    }

    private fun sendError(throwable: Throwable) {
        launch(mainContext, showLoading = false) {
            throwable.printStackTrace()
            if(throwable.message?.equals("null", true) == true) return@launch
            throwable.message?.let {
                errorFlow.emit(it)
            }
        }
    }

}