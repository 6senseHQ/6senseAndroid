package com.six.sense.domain.model

import android.graphics.Bitmap
import com.six.sense.presentation.screen.chat.Role

/**
 * Represents a single message in a conversation.
 *
 * This data class encapsulates all the information associated with a message,
 * including its textual content, an optional image, the timestamp it was sent,
 * and the role of the message sender.
 *
 * @property message The textual content of the message. Defaults to an empty string.
 * @property image An optional image associated with the message, represented as a Bitmap.
 *                 Can be null if no image is included.
 * @property time The timestamp indicating when the message was sent, represented as
 *                 the number of milliseconds since the epoch (January 1, 1970, 00:00:00 GMT).
 *                 Defaults to 0L.
 * @property role The role of the message sender. See the [Role] enum for possible values.
 *                 Defaults to [Role.USER].
 */
data class MessageData(
    /**
     * The textual content of the message.
     */
    val message: String = "",
    /**
     * An optional image associated with the message.
     */
    val image: Bitmap? = null,
    /**
     * The timestamp indicating when the message was sent.
     */
    val time: Long = 0L,
    /**
     * The role of the message sender.
     */
    val role: Role = Role.USER,
)
