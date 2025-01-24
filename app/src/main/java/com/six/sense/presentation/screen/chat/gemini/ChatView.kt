package com.six.sense.presentation.screen.chat.gemini

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.ai.client.generativeai.type.content
import com.six.sense.R
import com.six.sense.presentation.screen.chat.components.ChatMessageItem
import com.six.sense.presentation.screen.chat.components.ChatTextField
import com.six.sense.ui.theme.SixSenseAndroidTheme
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

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
                .padding(16.sdp),
            verticalAlignment = Alignment.Companion.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .requiredSize(48.sdp),
                contentAlignment = Alignment.Companion.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_polyline_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceContainer
                )
            }
            Spacer(modifier = Modifier.requiredWidth(10.sdp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.sdp)
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
fun ChatView(
    modifier: Modifier = Modifier,
    sendPrompt: (String) -> Unit,
    chatViewModel: ChatViewModel,
) {
    val chatUiState by chatViewModel.chatUiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val (chatText, setChatText) = remember { mutableStateOf("") }
    val chatHistory = remember { mutableStateListOf("") }
   /* val chat = remember {
        chatViewModel.generativeModel.startChat(
            listOf(
                content("user") {
                    text(chatText)
                },
                content("model") {
                    text("how are you?")
                }
            )
        )
    }*/
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            ChatBottom(
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.systemBars)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .imePadding(),
                chatText = chatText,
                setChatText = setChatText,
                sendPrompt = {
                    coroutineScope.launch {
                        chatHistory.add(chatText)

                        // Send the message to the model
                        val response = chat.sendMessage(chatText)

                        // Add model's response to chat history
                        chatHistory.add("${response.text}")
                    }
                    setChatText("")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .consumeWindowInsets(WindowInsets.systemBars)
                .padding(innerPadding)
                .fillMaxWidth()
                , contentPadding = PaddingValues(
                horizontal = 16.dp
                )
        ) {
            items(chatHistory.size) { index ->
                if (index % 2 == 0) {
                    Text(
                        modifier = Modifier,
                        text = chatHistory[index],
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium
                    )

                } else {
                    Column (modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.End)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    shape = CircleShape
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            text = chatHistory[index],
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBottom(
    modifier: Modifier = Modifier,
    chatText: String,
    setChatText: (String) -> Unit,
    sendPrompt: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.sdp, Alignment.End)
    ) {
        ChatTextField(
            modifier = Modifier
                .weight(1f),
            chatText = chatText, setChatText = setChatText,
            sendPrompt = sendPrompt
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DefPrev() {
    SixSenseAndroidTheme {
//        ChatView()
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