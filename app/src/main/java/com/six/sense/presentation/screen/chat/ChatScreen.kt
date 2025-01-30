package com.six.sense.presentation.screen.chat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.six.sense.presentation.screen.chat.components.ChatTextField
import com.six.sense.presentation.screen.chat.gemini.SystemInstructions
import ir.kaaveh.sdpcompose.sdp

/**
 * A composable function that renders a chat view.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    selectedModel: MutableState<Model>,
    showModelDialog: Boolean,
    sendPrompt: (String, Bitmap?) -> Unit,
    chatUiState: ChatUiState,
    onDismissRequest: () -> Unit,
    onClickAssistant: (String) -> Unit,
) {
    val (chatText, setChatText) = remember { mutableStateOf("") }
    var currentModel by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let { bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(it)) }
    }

    val listState = rememberLazyListState()
    LaunchedEffect(chatUiState.outputContent) {
        if (chatUiState.outputContent.isNotEmpty()) {
            listState.animateScrollToItem(chatUiState.chatHistory.lastIndex)
        }
    }
    Scaffold(modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Column {
                if (bitmap != null)
                    Box(
                        modifier = Modifier

                            .padding(horizontal = 16.dp)
                    ) {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(58.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        FilledIconButton(modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24 .dp), onClick = {
                            bitmap = null
                        }, colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )) {
                            Icon(Icons.Outlined.Close, null,modifier= Modifier.padding(3.dp),)
                        }
                    }
                ChatBottom(
                    modifier = Modifier
                        .consumeWindowInsets(WindowInsets.systemBars)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .imePadding(),
                    chatText = chatText,
                    setChatText = setChatText,
                    pickMedia = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
                    sendPrompt = { s ->
                        sendPrompt(s, bitmap)
                        setChatText("")
                        bitmap = null
                        keyboardController?.hide()
                    }
                )
            }
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .consumeWindowInsets(WindowInsets.systemBars)
                .padding(innerPadding)
                .fillMaxWidth(), contentPadding = PaddingValues(
                horizontal = 16.dp
            ), state = listState
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
    if (showModelDialog) {
        ModelSelection(
            selectedButton = currentModel,
            onSelectedButton = {
                currentModel = it
                onDismissRequest()
            },
            onDismissRequest = onDismissRequest,
            selectedModel = selectedModel,
            chatUiState = chatUiState,
            onClickAssistant = onClickAssistant
        )
    }
}

@Composable
fun ChatBottom(
    modifier: Modifier = Modifier,
    chatText: String,
    setChatText: (String) -> Unit,
    sendPrompt: (String) -> Unit,
    pickMedia: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.sdp, Alignment.End)
    ) {
        ChatTextField(
            modifier = Modifier.weight(1f),
            chatText = chatText,
            setChatText = setChatText,
            sendPrompt = sendPrompt,
            pickMedia = pickMedia
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ModelSelection(
    selectedButton: Int,
    selectedModel: MutableState<Model>,
    onSelectedButton: (Int) -> Unit,
    onClickAssistant: (String) -> Unit,
    onDismissRequest: () -> Unit,
    chatUiState: ChatUiState,
) {
    AlertDialog(
        title = {
            Text(text = "Chat Settings")
        }, onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text(text = "Close")
            }
        }, text = {
            Column {
                Text(
                    text = "Select Model",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(.7f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                ButtonGroup {
                    Model.entries.fastForEach { model ->
                        ToggleButton(
                            checked = model == selectedModel.value,
                            onCheckedChange = {
                                if (it) selectedModel.value = model
                            },
                            modifier = Modifier.weight(if (model == selectedModel.value) 1.3f else 1f)
                        ) {
                            Text(model.name, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                when (selectedModel.value) {
                    Model.Gemini -> {
                        Column {
                            Text(
                                text = "Select System Instructions",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(.7f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            SystemInstructions.entries.drop(1)
                                .forEachIndexed { index, instruction ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedButton == index,
                                            onClick = { onSelectedButton(selectedButton) })
                                        Text(text = instruction.role)
                                    }
                                }
                        }
                    }

                    Model.OpenAI -> {
                        Column {
                            Text(
                                text = "Select an Assistant",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(.7f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
//                            ButtonGroup {
                            chatUiState.assistants.forEachIndexed { index, assistant ->
//                                ToggleButton(
//                                    checked = assistant.id() == chatUiState.assistantId,
//                                    onCheckedChange = {
//                                        if(it) onClickAssistant(assistant.id())
//                                    },
//                                    modifier = Modifier.weight(if(assistant.id() == chatUiState.assistantId) 1.3f else 1f)
//                                ) {
//                                    Text(assistant.name().orElse("null"), style = MaterialTheme.typography.bodyMedium)
//                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = assistant.id() == chatUiState.assistantId,
                                        onClick = { onClickAssistant(assistant.id()) })
                                    Text(
                                        assistant.name().orElse("null"),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
//                                }
                            }
                        }
                    }
                }

            }
        }
    )
}

