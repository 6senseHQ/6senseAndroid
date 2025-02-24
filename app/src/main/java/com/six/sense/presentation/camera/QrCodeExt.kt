package com.six.sense.presentation.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Starts a QR code scanner using the camera and returns the scanned data.
 *
 * This function sets up a camera controller, configures a barcode scanner, and starts image analysis
 * to detect QR codes. It suspends until a QR code is successfully scanned, then returns the raw
 * value of the scanned code.
 *
 * @receiver [Fragment] and [PreviewView] The view that will display the camera preview.
 * @return The raw value of the scanned QR code as a [String].
 */
// In your main activity or fragment
context(Fragment)
@Suppress("CONTEXT_RECEIVERS_DEPRECATED")
suspend fun PreviewView.startQrScanner(): String = suspendCoroutine { continuation ->
    val cameraController = LifecycleCameraController(context)
    val barcodeScannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    val barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions)
    cameraController.unbind() // Unbind any previous bindings
    var job: Job? = null
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        MlKitAnalyzer(
            listOf(barcodeScanner),
            ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { result ->
            job?.cancel()
            job = lifecycleScope.launch{
                val qrData = withContext(Dispatchers.Default){
                    val barcodeResults = result?.getValue(barcodeScanner)
                    barcodeResults?.firstOrNull()?.rawValue
                }
                Log.d("TAG", "startQrScanner: $qrData")
                if (qrData.isNullOrEmpty())
                    overlay.clear()
                else{
                    barcodeScanner.close()
                    cameraController.clearImageAnalysisAnalyzer()
                    continuation.resume(qrData)
                }
            }
        }
    )
    cameraController.bindToLifecycle(this@Fragment)
    controller = cameraController
}

suspend fun Context.startQrScanner(): String {
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .enableAutoZoom()
        .build()
    val scanner = GmsBarcodeScanning.getClient(this, options)
    return scanner.startScan().await().rawValue.orEmpty()
}

