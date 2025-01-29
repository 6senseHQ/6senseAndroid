package com.six.sense.presentation.screen.chat.gemini

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.six.sense.presentation.screen.chat.components.ChatTextField
import com.six.sense.presentation.screen.chat.gemini.ImageResources.imageList
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

/**
 * A composable function that renders a chat view.
 * @param modifier [Modifier] Modifier for the layout.
 */
@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    sendPrompt: (String, Bitmap) -> Unit,
    chatUiState: ChatUiState,
) {
    val (chatText, setChatText) = remember { mutableStateOf("") }
    var isImagePickerVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var currentImage = remember { mutableIntStateOf(1) }

    val listState = rememberLazyListState()
    LaunchedEffect(chatUiState.outputContent) {
        if (chatUiState.outputContent.isNotEmpty()) {
            listState.animateScrollToItem(chatUiState.chatHistory.lastIndex)
        }
    }
    if (isImagePickerVisible) {
        ImagePickerSheet(
            isImagePickerVisible = isImagePickerVisible,
            onImagePickerVisible = { isImagePickerVisible = !isImagePickerVisible },
            currentImage = currentImage,
            onImageClick = {
                currentImage.intValue = it;
                BitmapFactory.decodeResource(
                    context.resources, imageList[it]
                )
            }
        )
    } else Scaffold(modifier = modifier,
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
                setBitMap = BitmapFactory.decodeResource(
                    context.resources, imageList[currentImage.intValue]
                ),
                openSheet = { isImagePickerVisible = !isImagePickerVisible },
                sendPrompt = { s,b->
                    sendPrompt(s,b)
                    setChatText("")
                }

                /*sendPrompt = {
                    sendPrompt(
                        chatText,
                        BitmapFactory.decodeResource(context.resources, imageList[currentImage])
                    ); setChatText("")
                },
                imagePosition = 2*/
            )
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
}

@Composable
fun ChatBottom(
    modifier: Modifier = Modifier,
    chatText: String,
    setChatText: (String) -> Unit,

    setBitMap: Bitmap,
    sendPrompt: (String, Bitmap) -> Unit,
    openSheet: () -> Unit,
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
            setBitMap = setBitMap,
            openSheet = openSheet
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
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(text = { Text("Show sheet") },
            icon = { Icon(Icons.Outlined.Add, contentDescription = "") },
            onClick = { showBottomSheet = true })
    }) { contentPadding ->
        Column(
            modifier = Modifier
        ) {
            if (showBottomSheet) ModalBottomSheet(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(), onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState
            ) {
                // Sheet content
                Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
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
                RadioButton(selected = selectedButton == index,
                    onClick = { onSelectedButton(selectedButton) })
                Text(text = instruction.role)
            }
        }
    }
}

/**
 * Image picker sheet
 *
 * @param modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerSheet(
    modifier: Modifier = Modifier, isImagePickerVisible: Boolean,
    onImagePickerVisible: (Boolean) -> Unit,
    currentImage: MutableIntState,
    onImageClick: (Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    if (isImagePickerVisible) {
        ModalBottomSheet(
            onDismissRequest = { onImagePickerVisible(false) }, sheetState = sheetState
        ) {
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(imageList.size) {
                    Image(
                        modifier = Modifier
                            .aspectRatio(1 / 1f)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable {
                                onImageClick(it)
                                onImagePickerVisible(false)
                            }
                            .border(
                                if (currentImage.intValue == it) BorderStroke(
                                    4.dp,
                                    MaterialTheme.colorScheme.primary
                                ) else BorderStroke(0.dp, MaterialTheme.colorScheme.background),
                            ),
                        painter = painterResource(imageList[it]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}