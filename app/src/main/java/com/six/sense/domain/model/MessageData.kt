package com.six.sense.domain.model

import android.graphics.Bitmap
import com.six.sense.presentation.screen.chat.Role

data class MessageData(
    val message: String = "",
    val image: Bitmap? = null,
    val time: Long = 0L,
    val role: Role = Role.USER
)
