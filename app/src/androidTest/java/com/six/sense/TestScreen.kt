package com.six.sense

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * Test screen
 *
 * @param modifier
 */
@Composable
fun TestScreen(modifier: Modifier = Modifier) {
    var isClicked by remember { mutableStateOf(false) }
    Button(modifier = modifier, onClick = {
        isClicked = true

    }) {
        Text(text = "Sign in with Google")
    }

    if (isClicked) {
        Text("Clicked!")
    }
}