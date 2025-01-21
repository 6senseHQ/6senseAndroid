package com.six.sense.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.openai.client.OpenAIClient
import com.openai.models.AssistantTool
import com.openai.models.BetaAssistantCreateParams
import com.openai.models.BetaThreadRunCreateParams
import com.openai.models.ChatCompletion
import com.openai.models.ChatCompletionCreateParams
import com.openai.models.ChatCompletionMessageParam
import com.openai.models.ChatCompletionUserMessageParam
import com.openai.models.ChatModel
import com.openai.models.FunctionTool
import com.openai.models.ModelListParams
import com.six.sense.data.local.datastore.DataStoreManager
import com.six.sense.presentation.base.BaseViewModel
import com.six.sense.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the main screen of the application.
 *
 * This ViewModel manages the state and logic for the main screen, including interactions with the OpenAI API.
 *
 * @property savedStateHandle SavedStateHandle for managing state across configuration changes.
 * @property openAIClient The client for interacting with the OpenAI API.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val openAIClient: OpenAIClient,
    private val dataStoreManager: DataStoreManager,
) : BaseViewModel() {
    /**
     * Ui mode state light by default
     */
    val isUiLightMode = dataStoreManager.readAsFlow("THEME_MODE",true)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    fun switchUiMode(isLightMode: Boolean) {
        viewModelScope.launch {
            dataStoreManager.save("THEME_MODE", isLightMode)
        }
    }

    /**
     * A function to test the OpenAI API.
     */
    fun testOpenAI(){
        launch(ioContext){
            val assistantId= openAIClient.beta().assistants().create(
                BetaAssistantCreateParams.builder()
                    .model(ChatModel.GPT_4O)
                    .instructions("")
                    .tools(listOf(AssistantTool.ofFunctionTool(FunctionTool.builder().type(FunctionTool.Type.FUNCTION).build())))
                    .build()
            ).id()
            openAIClient.beta().threads().runs().create(
                BetaThreadRunCreateParams.builder()
                    .assistantId(assistantId)
                    .instructions("")
                    .build()
            )

            openAIClient.models().list(
                ModelListParams.builder()
                    .build()
            ).data().map { it.id() }.log()
            val params = ChatCompletionCreateParams.builder()
                .messages(listOf(
                    ChatCompletionMessageParam.ofChatCompletionUserMessageParam(
                        ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.USER)
                        .content(ChatCompletionUserMessageParam.Content.ofTextContent("Hay, this is a test"))
                        .build())))
                .model(ChatModel.GPT_3_5_TURBO)
                .build()
            val chatCompletion: ChatCompletion = openAIClient.chat().completions().create(params)
            chatCompletion.choices().first().message().log()
        }

    }

}