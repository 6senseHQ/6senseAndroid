package com.six.sense.presentation.screen.materialComponents.camera

import android.view.SoundEffectConstants
import androidx.camera.compose.CameraXViewfinder
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.FlipCameraAndroid
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.six.sense.presentation.camera.startQrScanner
import com.six.sense.utils.bounceClick
import com.six.sense.utils.setClipBoardData
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch

@Composable
fun CameraXScreen(
    onNavigateToCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var qrData by rememberSaveable { mutableStateOf(null as String?) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ){
        Button(onClick = {
            onNavigateToCamera()
        }){
            Text(text = "Capture Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
//        capturedImageUri?.let { uri ->
//            AsyncImage(
//                painter = rememberAsyncImagePainter(uri),
//                contentDescription = "Captured Image",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
//                    .align(Alignment.TopEnd)
//                    .padding(16.dp),
//                contentScale = ContentScale.Crop
//            )
//        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                qrData = context.startQrScanner()
            }
        }){
            Text(text = "Scan QR Code")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Scanned data: $qrData")
    }
}


@Composable
fun CaptureScreen(
    cameraUiState: CameraUiState,
    onClickFlipCamera: () -> Unit,
    onClearQr: () -> Unit,
    onBack: () -> Unit,
    onCaptureImage: () -> Unit,
    modifier: Modifier = Modifier
) {
//    var isLoading by remember { mutableStateOf(false) }
    val view = LocalView.current


    Box(modifier = modifier) {
        // Camera Preview
        cameraUiState.surfaceRequest?.let { request ->
            CameraXViewfinder(
                surfaceRequest = request,
                modifier = modifier
            )
        }

        // Top Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.2f))
                .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                .padding(5.sdp)
        ) {
            // Back Button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp)
                    .align(Alignment.CenterStart)
                    .bounceClick()
            ) {
                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        // Bottom Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.2f))
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                )
                .padding(vertical = 14.sdp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flip Camera Button
                IconButton(
                    onClick = onClickFlipCamera,
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                        .bounceClick()
                ) {
                    Icon(Icons.Rounded.FlipCameraAndroid, contentDescription = "Flip Camera", tint = Color.White)
                }

                // Capture Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .bounceClick()
                        .size(70.dp)
                        .clickable {
//                            isLoading = true
                            onCaptureImage()
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                        }
                        .background(Color.White, shape = CircleShape)
                        .padding(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, shape = CircleShape)
                            .border(5.dp, Color.Gray, CircleShape)
                    )
                }

                // Save Button (Hidden by Default)
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                        .bounceClick()
                ) {
                    Icon(Icons.Rounded.Save, contentDescription = "Save", tint = Color.White)
                }
            }
        }

        // Image List
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.sdp)
                .align(Alignment.BottomCenter),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(cameraUiState.imageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Loading Indicator
//        if (isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.4f)),
//                contentAlignment = Alignment.Center
//            ) {
//                LoadingIndicator()
//            }
//        }
    }
    QRCodeCopyPopup(qrData = cameraUiState.qrData, onDismiss=onClearQr, modifier)
}

@Composable
fun QRCodeCopyPopup(
    qrData: String?,
    onDismiss: () -> Unit = {},
    modifier: Modifier
) {
    val context = LocalContext.current

    AnimatedVisibility(
        visible = qrData != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)), // Transparent background
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(.7f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QR Code Data",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = qrData.orEmpty(), textAlign = TextAlign.Center, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(12.dp))

                    IconButton(
                        onClick = {
                            context.setClipBoardData(qrData)
                            onDismiss()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = "Copy QR Code",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

