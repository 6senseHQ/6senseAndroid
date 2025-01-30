package com.six.sense.data.repo

import com.openai.client.OpenAIClient
import com.openai.models.Assistant
import com.openai.models.BetaAssistantListParams
import com.openai.models.BetaThreadCreateParams
import com.openai.models.BetaThreadDeleteParams
import com.openai.models.BetaThreadMessageCreateParams
import com.openai.models.BetaThreadMessageListParams
import com.openai.models.BetaThreadRunCreateParams
import com.openai.models.BetaThreadRunRetrieveParams
import com.openai.models.Message
import com.openai.models.RunStatus
import com.six.sense.data.local.room.ThreadDao
import com.six.sense.data.local.room.entities.ThreadsEntity
import com.six.sense.domain.repo.OpenAiRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

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

    override suspend fun createNewThread(): String {
        return withContext(dispatcher) {
//            val threadId = openAIClient.beta().threads().create(
//                BetaThreadCreateParams.builder().build()
//            ).id()
//            openAIClient.beta().threads().createAndRun(
//                BetaThreadCreateAndRunParams.builder()
//                    .assistantId(assistantId)
//                    .thread(
//                        BetaThreadCreateAndRunParams.Thread.builder().
//                    )
//                    .instructions("")
//                    .build()
//            )
            openAIClient.beta().threads().create(
                BetaThreadCreateParams.builder().build()
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
        assistantId: String,
        threadId: String,
        message: String
    ) {
        withContext(dispatcher) {
            if(message.isEmpty()) {
                openAIClient.beta().threads()
                    .delete(BetaThreadDeleteParams.builder().threadId(threadId).build())
                threadDao.deleteThread(threadId)
                return@withContext
            }
            openAIClient.beta().threads().messages().create(
                BetaThreadMessageCreateParams.builder()
                    .role(BetaThreadMessageCreateParams.Role.USER)
                    .threadId(threadId)
                    .content(message)
                    .build()
            ).content()
            val run = openAIClient.beta().threads().runs().create(
                BetaThreadRunCreateParams.builder()
                    .assistantId(assistantId)
                    .threadId(threadId)
                    .build()
            )
            while (true) {
                val updatedRun = openAIClient.beta().threads().runs().retrieve(
                    BetaThreadRunRetrieveParams.builder().threadId(threadId).runId(run.id()).build()
                )
                if (updatedRun.status() == RunStatus.COMPLETED)
                    break
                else if (updatedRun.status() == RunStatus.FAILED)
                    error("Generation failed!")
                delay(1.seconds)
            }
        }
    }
}