package com.six.sense.data.repo

import android.graphics.Bitmap
import androidx.compose.ui.util.fastFilteredMap
import com.openai.client.OpenAIClient
import com.openai.models.Assistant
import com.openai.models.BetaAssistantListParams
import com.openai.models.BetaThreadCreateParams
import com.openai.models.BetaThreadDeleteParams
import com.openai.models.BetaThreadMessageCreateParams
import com.openai.models.BetaThreadMessageListParams
import com.openai.models.BetaThreadRunCreateParams
import com.openai.models.BetaThreadRunRetrieveParams
import com.openai.models.ImageFile
import com.openai.models.ImageFileContentBlock
import com.openai.models.MessageContentPartParam
import com.openai.models.RunStatus
import com.openai.models.TextContentBlockParam
import com.six.sense.BuildConfig
import com.six.sense.data.local.room.ThreadDao
import com.six.sense.data.local.room.entities.ThreadsEntity
import com.six.sense.domain.model.MessageData
import com.six.sense.domain.repo.OpenAiRepo
import com.six.sense.presentation.screen.chat.asRole
import com.six.sense.utils.log
import com.six.sense.utils.toBitmap
import com.six.sense.utils.toByteArray
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.apache.http.entity.ContentType
import kotlin.time.Duration.Companion.seconds

class OpenAiRepoImpl(
    private val openAIClient: OpenAIClient,
    private val threadDao: ThreadDao,
    private val dispatcher: CoroutineDispatcher,
    private val ktorClient: HttpClient
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

    override suspend fun getThreadMessages(threadId: String): List<MessageData> {
        return withContext(dispatcher) {
            openAIClient.beta().threads().messages().list(
                BetaThreadMessageListParams.builder().threadId(threadId).build()
            ).data().map { msg ->
                val message = msg.content().fastFilteredMap( {it.isText() }, { it.asText().text().value() }).firstOrNull().orEmpty()
//                val image = msg.content().fastFilteredMap( {it.isImageFile() }, { it.asImageFile().imageFile().fileId() }).firstOrNull()?.let {
//                    openAIClient.files().retrieve(FileRetrieveParams.builder().fileId(it).build()).validate().bytes()
//                }?.toByteArray()?.toBitmap()
                val image = msg.content().fastFilteredMap( {it.isImageFile() }, { it.asImageFile().imageFile().fileId() }).firstOrNull()?.let {
                    downloadImageFile(it)
                }
                MessageData(
                    message = message,
                    image = image,
                    time = msg.createdAt(),
                    role = msg.role().value().name.asRole
                )
            }.reversed().apply {
                log("messages")
            }
        }
    }

    override suspend fun sendMessageToThread(
        assistantId: String,
        threadId: String,
        message: String,
        image: Bitmap?
    ) {
        withContext(dispatcher) {
            if(message.isEmpty()) {
                openAIClient.beta().threads()
                    .delete(BetaThreadDeleteParams.builder().threadId(threadId).build())
                threadDao.deleteThread(threadId)
                getAllThreads()
                return@withContext
            }
            openAIClient.beta().threads().messages().create(
                BetaThreadMessageCreateParams.builder()
                    .role(BetaThreadMessageCreateParams.Role.USER)
                    .threadId(threadId)
                    .content(message)
                    .apply {
//                        if(image == null) return@apply
//                        val fileId = uploadImageToOpenAI(image.toByteArray())
//                        addAttachment(Attachment.builder().fileId(fileId).tools(listOf(Tool.ofFileSearch())).build())
                        if(image == null)
                            content(message)
                        else {
                            val fileId = uploadImageToOpenAI(image.toByteArray())
                            content(BetaThreadMessageCreateParams.Content.ofArrayOfContentParts(
                                listOf(MessageContentPartParam.ofImageFile(ImageFileContentBlock.builder().imageFile(
                                    ImageFile.builder().fileId(fileId).detail(ImageFile.Detail.LOW).build()).build()),
//                                add(MessageContentPartParam.ofImageUrl(
//                                    ImageUrlContentBlock.builder().imageUrl(
//                                    ImageUrl.builder().url("data:image/png;base64,${image.toBase64()}").build()
//                                ).build()))
                                    MessageContentPartParam.ofText(TextContentBlockParam.builder().text(message).build())
                                )
                            ))
                        }
                    }.build()

            )
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


    private suspend fun uploadImageToOpenAI(imageBytes: ByteArray): String {
        val response: HttpResponse = ktorClient.submitFormWithBinaryData(
            url = "https://api.openai.com/v1/files",
            formData = formData {
                append("purpose", "vision")
                append("file", imageBytes, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=tmp.jpeg")
                    append(HttpHeaders.ContentType, ContentType.IMAGE_JPEG.toString()) // Corrected Content-Type
                })
            }
        ) {
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.OPENAI_API_KEY}")
        }
        val responseBody = response.body<JsonObject>()
        val fileId = responseBody["id"]?.jsonPrimitive?.content ?: error("Failed to upload image")
        return fileId
    }

    private suspend fun downloadImageFile(fileId: String): Bitmap? {
        val response = ktorClient.get("https://api.openai.com/v1/files/$fileId/content") {
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.OPENAI_API_KEY}")
        }
        val imageBytes = response.body<ByteArray>() // Get raw bytes
        imageBytes.log("imageBytes")
        return imageBytes.toBitmap()
    }

}