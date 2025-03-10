package com.six.sense.data.repo

import android.content.Context
import com.six.sense.BuildConfig
import com.six.sense.R
import com.six.sense.domain.repo.GeminiFilesRepo
import com.six.sense.utils.log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Implementation of the [GeminiFilesRepo] interface for uploading files to the Gemini API.
 *
 * This class handles the process of uploading a file (in this case, a raw resource)
 * to the Gemini API's file storage service using a resumable upload protocol.
 *
 * @property ktorClient The [HttpClient] instance used for making HTTP requests.
 * @property dispatcher The [CoroutineDispatcher] used for offloading the file upload operation to a background thread.
 * @property context The [Context] used to access application resources, such as the raw file.
 */
class GeminiFilesRepoImpl(
    private val ktorClient: HttpClient,
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
) : GeminiFilesRepo {
    /**
     * The base URL for the Generative Language API.
     */
    val baseUrl = "https://generativelanguage.googleapis.com"

    /**
     * The display name for the file being uploaded.
     */
    val displayName = "TEXT"

    /**
     * The bytes of the file being uploaded.
     */
    val fileBytes = context.resources.openRawResource(R.raw.login).readBytes()

    /**
     * The MIME type of the file being uploaded.
     */
    val mimeType = "text/plain" // Adjust based on your file type

    /**
     * The number of bytes in the file being uploaded.
     */
    val numBytes = fileBytes.size
    override suspend fun uploadFile(): String {
        return withContext(dispatcher) {

            val responseHeaders =
                ktorClient.post("$baseUrl/upload/v1beta/files?key=${BuildConfig.apiKey}") {
                    headers {
                        append("X-Goog-Upload-Protocol", "resumable")
                        append("X-Goog-Upload-Command", "start")
                        append("X-Goog-Upload-Header-Content-Length", numBytes.toString())
                        append("X-Goog-Upload-Header-Content-Type", mimeType)
                        // Add Authorization header if using OAuth
                        append(HttpHeaders.Authorization, "Bearer YOUR_OAUTH_TOKEN")
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }
                    setBody("""{"file": {"display_name": "$displayName"}}""")
                }
            responseHeaders.log()
            responseHeaders.headers.log()
            val uploadUrl =
                responseHeaders.headers["x-goog-upload-url"] ?: error("Upload URL not found")
            val uploadResponse = ktorClient.post(uploadUrl) {
                headers {
                    append(HttpHeaders.ContentLength, numBytes.toString())
                    append("X-Goog-Upload-Offset", "0")
                    append("X-Goog-Upload-Command", "upload, finalize")
                }
                setBody(fileBytes)
            }

            val fileInfo = uploadResponse.body<Map<String, Any>>()
            val fileUri =
                (fileInfo["file"] as Map<*, *>)["uri"] as? String ?: error("File URI not found")

            fileUri
        }
    }
}