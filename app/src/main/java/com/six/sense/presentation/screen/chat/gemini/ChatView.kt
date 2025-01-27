package com.six.sense.presentation.screen.chat.gemini

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.six.sense.presentation.screen.chat.components.ChatTextField
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

/**
 * A composable function that renders a chat view.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    showModelDialog: Boolean,
    sendPrompt: (String) -> Unit,
    chatUiState: ChatUiState,
) {
    val (chatText, setChatText) = remember { mutableStateOf("") }
    var currentModel by remember { mutableIntStateOf(0) }

    val listState = rememberLazyListState()
    LaunchedEffect(chatUiState.outputContent) {
        if (chatUiState.outputContent.isNotEmpty()) {
            listState.animateScrollToItem(chatUiState.chatHistory.lastIndex)
        }
    }
    if (showModelDialog) {
        ModelSelection(selectedButton = currentModel, onSelectedButton = { currentModel = it })
    } else
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
                    sendPrompt = { sendPrompt(chatText); setChatText("") }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.systemBars)
                    .padding(innerPadding)
                    .fillMaxWidth(), contentPadding = PaddingValues(
                    horizontal = 16.dp
                ),
                state = listState
            ) {
                items(chatUiState.chatHistory.size) { index ->
                    if (index % 2 != 0) {
                        Text(
                            modifier = Modifier,
                            text = chatUiState.chatHistory[index],
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceContainer,
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                text = chatUiState.chatHistory[index],
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBottomSheet(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()

    /**
     * Coroutine Scope.
     */
    val scope = rememberCoroutineScope()

    /**
     * Show bottom sheet.
     */
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show sheet") },
                icon = { Icon(Icons.Outlined.Add, contentDescription = "") },
                onClick = { showBottomSheet = true }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
        ) {
            if (showBottomSheet)
                ModalBottomSheet(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally), onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
                        Text(
                            text = "Hide bottom sheet"
                        )
                    }
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelection(
    modifier: Modifier = Modifier,
    selectedButton: Int,
    onSelectedButton: (Int) -> Unit,
) {
    BasicAlertDialog(onDismissRequest = {}) {
        SystemInstructions.entries.forEachIndexed { index, instruction ->
            Row {
                RadioButton(
                    selected = selectedButton == index,
                    onClick = { onSelectedButton(selectedButton) })
                Text(text = instruction.role)
            }
        }
    }
}