package com.six.sense.presentation.screen.chat.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.dp

/**
 * A composable function that renders a chat text field.
 * @param modifier [Modifier] Modifier for the layout.
 * @param chatText [String] The text to be displayed in the chat text field.
 * @param setChatText [Unit] A function to set the text in the chat text field.
 */
@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    chatText: String = "",
    setChatText: (String) -> Unit = {},
) {
    TextField(
        modifier = modifier, value = chatText, onValueChange = setChatText,
        colors = TextFieldDefaults.colors(focusedIndicatorColor = Transparent, unfocusedIndicatorColor = Transparent),
        shape = RoundedCornerShape(28.dp),
        placeholder = {Text(text = "Message", style = MaterialTheme.typography.bodyMedium) },
        textStyle = MaterialTheme.typography.bodyMedium
    )
}