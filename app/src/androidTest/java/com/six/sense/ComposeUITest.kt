package com.six.sense

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyComposeUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Compose ui component test
     *
     */
    @Test
    fun composeUIComponentTest() {
        composeTestRule.setContent {
            TestScreen()
        }

        composeTestRule.onNodeWithText("Sign in with Google").performClick()

        composeTestRule.onNodeWithText("Clicked!", useUnmergedTree = true).assertIsDisplayed()
    }
}