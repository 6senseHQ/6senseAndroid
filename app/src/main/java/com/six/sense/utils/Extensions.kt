package com.six.sense.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.six.sense.utils.ButtonState.Idle
import com.six.sense.utils.ButtonState.Pressed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Extension function to log any object with a tag.
 *
 * This function logs the object's value and its class name to the Android Logcat.
 *
 * @receiver Any? The object to be logged.
 * @param tag The tag to be used for the log message. Defaults to "TAG".
 * @return The object itself, allowing for chaining.
 */
fun Any?.log(tag: String = "TAG"): Any? {
    Log.d("log> '$tag'", "$tag - $this : ${this?.javaClass?.name?.split('.')?.lastOrNull() ?: ""}")
    return this
}

/**
 * Collects a [Flow] within the lifecycle of a [LifecycleOwner], ensuring that the collection
 * only happens when the lifecycle is in the specified [minActiveState].
 *
 * This function uses `repeatOnLifecycle` to automatically handle starting and stopping the
 * collection based on the lifecycle state. It also filters out null values from the flow.
 *
 * @param T The type of data emitted by the flow.
 * @param context The coroutine context to use for launching the coroutine. Defaults to
 *   [EmptyCoroutineContext].
 * @param minActiveState The minimum lifecycle state required for the collection to be active.
 *   Defaults to [Lifecycle.State.RESUMED].
 * @param block A suspend function that will be called with each non-null value emitted by the
 *   flow.
 *
 * @receiver [LifecycleOwner] The lifecycle owner that this function is called on.
 * @receiver [Flow] The flow that this function is called on.
 */
context(LifecycleOwner)
fun <T> Flow<T?>.collectWithLifecycle(
    context: CoroutineContext = EmptyCoroutineContext,
    minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
    block: suspend CoroutineScope.(T) -> Unit,
) {
    lifecycleScope.launch(context) {
        repeatOnLifecycle(minActiveState) {
            filterNotNull().collect { value ->
                if (lifecycle.currentState.isAtLeast(minActiveState))
                    block(value)
            }
        }
    }
}

/**
 * Creates a bounce click effect for a composable.
 *
 * This modifier adds a scaling animation to the composable when it's clicked,
 * creating a "bounce" effect. It also handles click events and invokes the
 * provided [onClick] lambda.
 *
 * @receiver The [Modifier] to which this bounce click effect is applied.
 * @param onClick The callback to be invoked when the composable is clicked. Defaults to an empty lambda.
 * @return The modified [Modifier] with the bounce click effect.
 */
fun Modifier.bounceClick(onClick: () -> Unit = {}): Modifier = composed {
    var buttonState by remember { mutableStateOf(Idle) }
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(if (buttonState == Pressed) 0.90f else 1f, label = "")
    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                scope.launch {
                    delay(150L)
                    onClick()
                }
            }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == Pressed) {
                    waitForUpOrCancellation()
                    Idle
                } else {
                    awaitFirstDown(false)
                    Pressed
                }
            }
        }
}

/**
 * Represents the state of the button for the bounce click effect.
 *
 * [Pressed]: The button is currently being pressed.
 * [Idle]: The button is in its normal, unpressed state.
 */
enum class ButtonState {
    Pressed,
    Idle
}

fun Bitmap.toBase64(format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 80): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(format, quality, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Bitmap.toByteArray(format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 80): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(format, quality, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}

fun Long.toByteArray(): ByteArray {
    return ByteBuffer.allocate(Long.SIZE_BYTES).putLong(this).array()
}

fun ByteArray.toBitmap(): Bitmap? {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

fun Context?.getClipBoardData(): String {
    this ?: return ""
    val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var data = ""
    if (clipBoardManager.primaryClip?.description?.hasMimeType("text/*") == true) {
        clipBoardManager.primaryClip?.itemCount?.let {
            for (i in 0 until it) {
                data += clipBoardManager.primaryClip?.getItemAt(i)?.text ?: ""
            }
        }
    }
    data.log("getClipBoardData")
    return data
}

fun Context.setClipBoardData(data: String?) {
    data?.let {
        val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(data, data)
        clipBoardManager.setPrimaryClip(clip)
        Toast.makeText(this, "$data Copied!", Toast.LENGTH_SHORT).show()
    }
}