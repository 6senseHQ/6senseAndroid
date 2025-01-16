package com.six.sense.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

object AppNotifications {
    @RequiresApi(Build.VERSION_CODES.O)
    val notificationChannel = NotificationChannel(
        "channel_id",
        "channel_name",
        NotificationManager.IMPORTANCE_HIGH
    )
}
