package com.six.sense.data.repo

import com.openai.client.OpenAIClient
import com.openai.models.Assistant
import com.openai.models.BetaAssistantListParams
import com.openai.models.BetaThreadCreateAndRunParams
import com.openai.models.BetaThreadMessageCreateParams
import com.openai.models.BetaThreadMessageListParams
import com.openai.models.Message
import com.openai.models.MessageContent
import com.six.sense.data.local.room.ThreadDao
import com.six.sense.data.local.room.entities.ThreadsEntity
import com.six.sense.domain.repo.OpenAiRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class OpenAiRepoImpl(
    private val openAIClient: OpenAIClient,
    private val threadDao: ThreadDao,
    private val dispatcher: CoroutineDispatcher
) : OpenAiRepo {

    override val assistants: MutableStateFlow<List<Assistant>> = MutableStateFlow(emptyList())
    override val threads: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    override suspend fun getAllAssistants(): List<Assistant> {
        return withContext(dispatcher) {
            openAIClient.beta().assistants().list(
                BetaAssistantListParams.builder()
                    .build()
            ).data().also {
                assistants.value = it
            }
        }
    }

    override suspend fun createNewThread(assistantId: String): String {
        return withContext(dispatcher) {
            openAIClient.beta().threads().createAndRun(
                BetaThreadCreateAndRunParams.builder()
                    .assistantId(assistantId)
                    .instructions("Help me about the knowledge of the company")
                    .build()
            ).id().also {
                threadDao.insertData(ThreadsEntity(threadId = it))
                getAllThreads()
            }
        }
    }

    override suspend fun getAllThreads(): List<String> {
        return withContext(dispatcher) {
            threadDao.getAllThreads().map { it.threadId }.reversed().also {
                threads.value = it
            }
        }
    }

    override suspend fun getThreadMessages(threadId: String): List<Message> {
        return withContext(dispatcher) {
            openAIClient.beta().threads().messages().list(
                BetaThreadMessageListParams.builder().threadId(threadId).build()
            ).data()
        }
    }

    override suspend fun sendMessageToThread(
        threadId: String,
        message: String
    ): List<MessageContent> {
        return withContext(dispatcher) {
            openAIClient.beta().threads().messages().create(
                BetaThreadMessageCreateParams.builder()
                    .threadId(threadId)
                    .content("")
                    .build()
            ).content()
        }
    }
}