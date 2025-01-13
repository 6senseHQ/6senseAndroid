package com.six.sense.presentation.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.six.sense.R
import com.six.sense.ui.theme.SixSenseAndroidTheme
import kotlin.text.trim

/**
 * A composable function that renders a chat header.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
private fun ChatHeader(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .requiredSize(48.dp),
                contentAlignment = Alignment.Companion.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_polyline_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceContainer
                )
            }
            Spacer(modifier = Modifier.requiredWidth(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "6Sense", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Your personal assistant",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * A composable function that renders a chat message item.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
fun ChatMessageItem(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Companion.Center
        ) {
            Icon(
                modifier = Modifier.requiredSize(16.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_polyline_outline),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceContainer
            )
        }
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                text = "Hello World\nHow are you?".trim(' ', '.'),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * A composable function that renders a chat view.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
fun ChatView(modifier: Modifier = Modifier, sendPrompt: () -> Unit = {}) {
    Column(modifier = modifier.fillMaxSize()) {
        ChatHeader()
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChatMessageItem(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .align(Alignment.Companion.Start)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Companion.End)
            ) {
                ChatTextField(
                    modifier = Modifier.weight(1f)
                )
                FilledIconButton(modifier = Modifier.size(56.dp), onClick = {}) {
                    Icon(
                        modifier = Modifier
                            .offset(x = 2.dp)
                            .size(32.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_send_outline),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

/**
 * A composable function that renders a chat text field.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
fun ChatTextField(modifier: Modifier = Modifier) {
    val (chatText, setChatText) = remember { mutableStateOf("") }
    TextField(
        modifier = modifier, value = chatText, onValueChange = setChatText,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent
        ),
        shape = RoundedCornerShape(28.dp),
        placeholder = {
            Text(
                text = "Message",
                style = MaterialTheme.typography.bodySmall
            )
        },
        textStyle = MaterialTheme.typography.bodySmall
    )
}

@Preview(showBackground = true)
@Composable
private fun DefPrev() {
    SixSenseAndroidTheme {
        ChatView()
    }
}

@Preview(showBackground = true)
@Composable
private fun DefPrevTwo() {
    SixSenseAndroidTheme {
        ChatMessageItem()
    }
}

@Preview(showBackground = true)
@Composable
private fun DefPrevThree() {
    SixSenseAndroidTheme {
        ChatTextField()
    }
}