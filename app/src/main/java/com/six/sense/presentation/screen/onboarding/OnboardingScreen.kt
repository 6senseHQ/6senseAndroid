package com.six.sense.presentation.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.six.sense.R

/**
 * A composable function that displays an onboarding flow to the user.
 *
 * This screen guides the user through a series of steps (pages) to introduce
 * them to the application's features or gather initial information.
 *
 * The onboarding flow consists of three pages:
 *   - Page 0: [OnboardingPageOne]
 *   - Page 1: [OnboardingPageTwo]
 *   - Page 2: [OnboardingPageThree]
 *
 * Navigation between pages is handled internally, and the `onBoardingCompleted`
 * callback is invoked when the user completes the final page.
 *
 * @param modifier The modifier to apply to this layout.
 * @param onBoardingCompleted A callback function that is invoked when the user
 *   has completed the entire onboarding process.
 *
 * Example Usage:
 * ```
 * OnboardingScreen(onBoardingCompleted = {
 *     // Navigate to the main screen or perform other actions
 *     // after onboarding is finished.
 *     Log.d("Onboarding", "Onboarding Completed!")
 * })
 * ```
 */
@Composable
fun OnboardingScreen(modifier: Modifier = Modifier, onBoardingCompleted: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(0) }
    Scaffold {
        when (currentPage) {

            0 -> OnboardingPageOne(
                modifier = Modifier.padding(it),
                btnState = true,
                btnNext = { currentPage = 1 })

            1 -> OnboardingPageTwo(
                modifier = Modifier.padding(it),
                btnState = true,
                btnNext = { currentPage = 2 },
                btnPrev = { currentPage = 0 })

            2 -> OnboardingPageThree(
                modifier = Modifier.padding(it),
                btnState = true,
                btnNext = {
                    currentPage = 2; onBoardingCompleted()
                }
            )
        }
    }
}

@Composable
fun OnboardingPageOne(
    modifier: Modifier = Modifier,
    btnNext: () -> Unit = {},
    btnState: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Image(bitmap = ImageBitmap.imageResource(R.drawable.ob_welcome), null)
            Text(
                "Welcome to 6sense Boilerplate,", modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            Text("Your journey to seamless integration of components and library starts here.")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClick = btnNext,
            enabled = btnState
        ) {
            Text("Click to learn more")
        }
    }
}

@Composable
fun OnboardingPageTwo(
    modifier: Modifier = Modifier,
    btnNext: () -> Unit = {},
    btnPrev: () -> Unit = {},
    btnState: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Image(bitmap = ImageBitmap.imageResource(R.drawable.ob_organize), null)
            Text(
                "What is 6sense Boilerplate?", modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            Text("6sense Boilerplate is designed to help you manage your tasks effortlessly. With [key feature 1], [key feature 2], and [key feature 3], we make [insert value proposition, e.g., \"your life simpler,\" \"your goals achievable,\" \"your connections stronger.")
        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = btnPrev, enabled = btnState) {
                Icon(Icons.Outlined.ArrowBackIosNew, null)
            }
            TextButton(onClick = btnNext, enabled = btnState) {
                Text("Next")
            }

        }
    }
}

@Composable
fun OnboardingPageThree(
    modifier: Modifier = Modifier,
    btnNext: () -> Unit = {},
    btnState: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Image(bitmap = ImageBitmap.imageResource(R.drawable.ob_get_started), null)
            Text(
                "Ready to Get Started?", modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
            Text("oin thousands of users who are already with 6sense Boilerplate. It’s quick, easy, and free!")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClick = btnNext,
            enabled = btnState
        ) {
            Text("Let's get started")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefPrev() {
//    OnboardingScreen()
}