package com.six.sense.data.repo

import com.six.sense.domain.repo.MockKRepo

/**
 * Represents a mock user for testing or demonstration purposes.
 *
 * This data class encapsulates the essential information of a user, including their
 * username, password, and a unique user ID.  It's primarily designed to be used in
 * scenarios where a real user object is not required or available, such as in unit tests,
 * integration tests, or sample data creation.
 *
 * @property userName The username of the mock user.
 * @property password The password of the mock user.  Note that in real-world applications,
 *                   passwords should be handled securely (hashed and salted). This is a
 *                   simplified representation for mock data.
 * @property userId A unique integer identifier for the mock user.
 */
data class MockUser(
    val userName: String,
    val password: String,
    val userId: Int,
)

/**
 * [MockKRepoImpl] is a concrete implementation of the [MockKRepo] interface.
 * It provides mocked responses for user login and data fetching operations.
 * This class is primarily used for testing purposes where interactions with a real
 * repository are not desired or are impractical.
 */
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