package com.six.sense.domain.repo

import kotlinx.coroutines.flow.Flow

interface WebSocketRepo {
    suspend fun connect(): Boolean
    suspend fun disconnect()
    suspend fun observeMessages(): Flow<String>
    suspend fun sendMessage(message: String)
}