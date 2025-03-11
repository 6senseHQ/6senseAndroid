package com.six.sense.domain.repo

/**
 * Interface defining methods for interacting with a mocked repository.
 * This repository is intended for testing purposes and simulates
 * data access operations without relying on a real database or API.
 */
interface MockKRepo {
    fun loginUser(userName: String, password: String, userId: Int): String

    fun fetchUserData(userId: Int): String
}