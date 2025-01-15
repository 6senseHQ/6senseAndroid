package com.six.sense.presentation.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.six.sense.R
import com.six.sense.presentation.screen.chat.components.ChatMessageItem
import com.six.sense.presentation.screen.chat.components.ChatTextField
import com.six.sense.ui.theme.SixSenseAndroidTheme

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
 * A composable function that renders a chat view.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
fun ChatView(modifier: Modifier = Modifier, sendPrompt: () -> Unit = {}) {
    val (chatText, setChatText) = remember { mutableStateOf("") }
    Scaffold(
        modifier = modifier,
        topBar = { ChatHeader() },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(WindowInsets.systemBars)
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            ChatMessageItem(
                modifier = Modifier
                    .fillMaxWidth(.9f),
                itemResponseText = LoremIpsum(words = 20).values.joinToString(" ")
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Companion.End)
            ) {
                ChatTextField(
                    modifier = Modifier.weight(1f),
                    chatText = chatText, setChatText = setChatText
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