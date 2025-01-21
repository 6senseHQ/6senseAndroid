package com.six.sense.utils

import android.util.Log
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
    }.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {
            scope.launch {
                delay(150L)
                onClick()
            }
        }
    ).pointerInput(buttonState) {
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