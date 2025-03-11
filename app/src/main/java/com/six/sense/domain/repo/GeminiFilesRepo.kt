package com.six.sense.domain.repo

/**
 * Interface for interacting with a Gemini Files repository.
 * This interface defines methods for managing files within the Gemini system.
 */
interface GeminiFilesRepo {
    suspend fun uploadFile(): String
}