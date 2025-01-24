package com.six.sense.domain.repo

interface GeminiFilesRepo {
    suspend fun uploadFile(): String
}