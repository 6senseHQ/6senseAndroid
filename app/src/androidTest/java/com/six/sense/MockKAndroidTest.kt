package com.six.sense

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.six.sense.data.repo.MockUser
import com.six.sense.domain.repo.MockKRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Android test for MockK.
 */
@RunWith(AndroidJUnit4::class)
class MockKAndroidTest {
    /**
     * Test function with MockK.
     */
    @Test
    fun testWithMockK() = runTest {
        val mockRepo = mockk<MockKRepo>()
        val expectedUser = MockUser("John", "", 2)
        coEvery { mockRepo.loginUser(any(), any(), any()) }
            .returns(expectedUser.toString())
        coEvery { mockRepo.fetchUserData(2) }
            .returns(expectedUser.toString())

//        coVerify { mockRepo.getUser() }

    }
}