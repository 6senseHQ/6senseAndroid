package com.six.sense.data.repo

import com.six.sense.domain.repo.WebSocketRepo
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

/**
 * Implementation of the [WebSocketRepo] interface responsible for managing the WebSocket connection
 * and communication with the server.
 *
 * This class handles the following:
 * - Establishing and closing a WebSocket connection.
 * - Receiving messages from the server.
 * - Sending messages to the server.
 *
 * @property webSocketClient The [HttpClient] instance used for creating the WebSocket connection.
 * It should be configured with the WebSocket plugin.
 */
class WebSocketRepoImpl @Inject constructor(
    private val webSocketClient: HttpClient,
) : WebSocketRepo {
    private var session: WebSocketSession? = null
    override suspend fun connect(): Boolean {
        session = webSocketClient.webSocketSession { url("ws://localhost:8080/chat") }
        return session?.isActive == true
    }

    override suspend fun disconnect() {
        session?.close()
        session = null
    }

    override suspend fun observeMessages(): Flow<String> = flow {
        while (session?.isActive == true) {
            val frame = session?.incoming?.receive()
            val messages = frame?.readBytes()?.decodeToString()
            emit(messages ?: "n/a")
        }
    }

    override suspend fun sendMessage(message: String) {
        session?.send(Frame.Text(message))
    }
}
