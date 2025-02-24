package com.six.sense.presentation.screen.materialComponents.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraXViewModel @Inject constructor() : BaseViewModel() {
    // Used to set up a link between the Camera and your UI.
//    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
//    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest
//
//    private val _imageUris = MutableStateFlow(listOf<Uri>())
//    val imageUris = _imageUris.asStateFlow()
//
//    private val _qrData = MutableStateFlow<String?>(null)
//    val qrData = _qrData.asStateFlow()

    private val _cameraUiState = MutableStateFlow(CameraUiState())
    val cameraUiState = _cameraUiState.asStateFlow()


    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _cameraUiState.update { it.copy(surfaceRequest = newSurfaceRequest) }
        }
    }
    private val imageCaptureUseCase = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // Fast capture
        .build()

//    private val videoCaptureUseCase = VideoCapture.Builder()
//        .setTargetResolution(Size(1920, 1080)) // Set video resolution
//        .build()

    private fun getQRAnalyzerUseCase(context: Context): ImageAnalysis{
        val barcodeScannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        var job: Job? = null
        val barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions)
        return ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) // Avoid lag
            .build().apply {
                setAnalyzer(ContextCompat.getMainExecutor(context),
                    MlKitAnalyzer(
                        listOf(barcodeScanner),
                        ImageAnalysis.COORDINATE_SYSTEM_SENSOR,
                        ContextCompat.getMainExecutor(context)
                    ) { result ->
                        job?.cancel()
                        job = launch (showLoading = false) {
                            val qrData = withContext(Dispatchers.Default){
                                val barcodeResults = result?.getValue(barcodeScanner)
                                barcodeResults?.firstOrNull()?.rawValue
                            }
                            if (!qrData.isNullOrEmpty()){
                                Log.d("TAG", "startQrScanner: $qrData")
                                _cameraUiState.update { it.copy(qrData = qrData) }
//                                barcodeScanner.close()
//                                cameraController.clearImageAnalysisAnalyzer()
//                                continuation.resume(qrData)
                            }
                        }
                    }
                )
            }
    }

    fun clearQrData(){
        _cameraUiState.update { it.copy(qrData = null) }
    }

    private var processCameraProvider: ProcessCameraProvider? = null
    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        processCameraProvider?.unbindAll()
        processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider?.bindToLifecycle(
            lifecycleOwner, cameraUiState.value.cameraSelector, cameraPreviewUseCase, imageCaptureUseCase, getQRAnalyzerUseCase(appContext)
        )
        // Cancellation signals we're done with the camera
        try { awaitCancellation() } finally { processCameraProvider?.unbindAll() }
    }

    fun captureImage(context: Context) {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            File(context.cacheDir, "captured_image.jpg")
        ).build()

        imageCaptureUseCase.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        _cameraUiState.update { it.copy(imageUris = it.imageUris + uri) }
                    }
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraCapture", "Image capture failed: ${exception.message}")
                }
            }
        )
    }

    fun flipCamera(){
        _cameraUiState.update {
            it.copy(
                cameraSelector = if(it.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    CameraSelector.DEFAULT_BACK_CAMERA
            )
        }
    }

}

data class CameraUiState(
    val surfaceRequest: SurfaceRequest? = null,
    val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    val imageUris: List<Uri> = listOf(),
    val qrData: String? = null
)