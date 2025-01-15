package com.six.sense.data.repo

import com.six.sense.domain.repo.MockKRepo

data class MockUser(
    val userName: String,
    val password: String,
    val userId: Int,
)

class MockKRepoImpl : MockKRepo {
    override fun loginUser(userNane: String, password: String, userId: Int): String {
        return "okay"
    }

    override fun fetchUserData(userId: Int): String {
        return when (userId) {
            1 -> "user one"
            2 -> "user two"
            3 -> "user three"
            else -> "user not found"
        }
    }
}