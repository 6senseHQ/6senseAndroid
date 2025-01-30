package com.six.sense.presentation.screen.chat.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.six.sense.R
import com.six.sense.utils.bounceClick
import ir.kaaveh.sdpcompose.sdp

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
    sendPrompt: (String) -> Unit,
    pickMedia: () -> Unit,
) {
    TextField(
        modifier = modifier, value = chatText, onValueChange = setChatText,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent
        ),
        shape = RoundedCornerShape(28.sdp),
        placeholder = { Text(text = "Message", style = MaterialTheme.typography.bodyMedium) },
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = {
            Row {
                IconButton(
                    modifier = Modifier
                        .bounceClick(),
                    onClick = pickMedia
                ) {
                    Icon(
                        modifier = Modifier
                            .rotate(45f),
                        imageVector = ImageVector.vectorResource(R.drawable.attach_file_24px),
                        contentDescription = null
                    )
                }

                FilledIconButton(modifier = Modifier
                    .bounceClick(),
                    onClick = { sendPrompt(chatText.trimEnd()) }) {
                    Icon(
                        modifier = Modifier
                            .offset(x = 2.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_send_outline),
                        contentDescription = null
                    )
                }

            }
        },
        singleLine = true
    )
}