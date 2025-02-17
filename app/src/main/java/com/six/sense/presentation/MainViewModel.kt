package com.six.sense.presentation

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.openai.client.OpenAIClient
import com.openai.models.AssistantTool
import com.openai.models.BetaAssistantCreateParams
import com.openai.models.BetaThreadCreateAndRunParams
import com.openai.models.BetaThreadMessageCreateParams
import com.openai.models.BetaThreadMessageListParams
import com.openai.models.BetaThreadRunCreateParams
import com.openai.models.ChatCompletion
import com.openai.models.ChatCompletionContentPart
import com.openai.models.ChatCompletionContentPartImage
import com.openai.models.ChatCompletionContentPartImage.ImageUrl
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
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
    val firebaseAnalytics: FirebaseAnalytics,
) : BaseViewModel() {
    var keepSplashOpened: Boolean = true

    val showChatDialog = MutableStateFlow(false)

    var onBoardingCompleted = true

    var bitmap: Bitmap? = null

    /**
     * Ui mode state light by default
     */
    val isUiLightMode = dataStoreManager.readAsFlow("THEME_MODE", true).onEach {
        keepSplashOpened = false
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    fun switchUiMode(isLightMode: Boolean) {
        viewModelScope.launch {
            dataStoreManager.save("THEME_MODE", isLightMode)
        }
    }

    val showOnboarding = dataStoreManager.readAsFlow<Boolean?>("onboarding", null).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), null
    )

    /**
     * A function to test the OpenAI API.
     */
    fun testOpenAI() {
        launch(ioContext) {
            val assistantId = openAIClient.beta().assistants().create(
                BetaAssistantCreateParams.builder()
                    .model(ChatModel.GPT_4O)
                    .instructions("")
                    .tools(listOf(AssistantTool.ofFunction(FunctionTool.builder().build())))
                    .build()
            ).id()
            val threadId = openAIClient.beta().threads().createAndRun(
                BetaThreadCreateAndRunParams.builder()
                    .assistantId(assistantId)
                    .instructions("Help me about the knowledge of the company")
                    .build()
            ).id()
            openAIClient.beta().threads().messages().create(
                BetaThreadMessageCreateParams.builder()
                    .threadId(threadId)
                    .content("")
                    .build()
            ).content()
            val x = openAIClient.beta().threads().runs().create(
                BetaThreadRunCreateParams.builder()
                    .assistantId(assistantId)
                    .instructions("")
                    .build()
            )
            val list = openAIClient.beta().threads().messages().list(
                BetaThreadMessageListParams.builder().threadId(threadId).build()
            ).data()

            openAIClient.models().list(
                ModelListParams.builder()
                    .build()
            ).data().map { it.id() }.log()
            val params = ChatCompletionCreateParams.builder()
                .messages(
                    listOf(
                        ChatCompletionMessageParam.ofUser(
                            ChatCompletionUserMessageParam.builder()
                                .content(ChatCompletionUserMessageParam.Content.ofText("Hay, this is a test"))
                                .content(
                                    ChatCompletionUserMessageParam.Content.ofArrayOfContentParts(
                                        listOf(
                                            ChatCompletionContentPart.ofImageUrl(
                                                ChatCompletionContentPartImage.builder().imageUrl(
                                                    ImageUrl.builder().url("base64").build()
                                                ).build()
                                            )
                                        )
                                    )
                                )
                                .build()
                        )
                    )
                )
                .model(ChatModel.GPT_3_5_TURBO)
                .build()
            val chatCompletion: ChatCompletion = openAIClient.chat().completions().create(params)
            chatCompletion.choices().first().message().log()
        }

    }

}