package com.six.sense.domain.repo

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing WebSocket connections and communication.
 *
 * This interface provides methods for establishing, maintaining, and closing a WebSocket connection,
 * as well as observing incoming messages and sending outgoing messages.
 */
interface WebSocketRepo {
    suspend fun connect(): Boolean
    suspend fun disconnect()
    suspend fun observeMessages(): Flow<String>
    suspend fun sendMessage(message: String)
}