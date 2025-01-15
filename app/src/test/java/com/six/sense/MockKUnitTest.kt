package com.six.sense

import com.google.common.truth.Truth.assertThat
import com.six.sense.domain.repo.MockKRepo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

/**
 * Unit test for MockK.
 */
class MockKUnitTest {
    /**
     * Test function with MockK.
     */
    @Test
    fun `test function with mockK`() {
        /**
         * Create a mock object of MockKRepo.
         */
        val mockRepo = mockk<MockKRepo>()

        /**
         * Set up the expected behavior of the mock object.
         *
         */
        val expectedUser = mockRepo.loginUser("rana", "rana123", 1).apply {
                assertThat(this).isEqualTo("rana")
            }
        /**
         * Call the getUser() function on the mock object.
         * use [coEvery] for coroutine testing.
         * use [every] for regular testing.
         */
        every { mockRepo.loginUser("rana", "rana123", 1) } returns expectedUser
        verify {}
//        coEvery { mockRepo.getUser() } returns (expectedUser)
//        confirmVerified("test")
    }

}