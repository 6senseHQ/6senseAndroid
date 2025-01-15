package com.six.sense.domain.repo



interface MockKRepo {
    fun loginUser(userName: String, password: String, userId: Int): String

    fun fetchUserData(userId: Int): String
}