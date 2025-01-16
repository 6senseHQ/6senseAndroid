package com.six.sense

import android.app.Application
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.six.sense.presentation.screen.worker.setupWorkManagerOnAppStart
import com.six.sense.utils.AppNotifications
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application class for the 6Sense app.
 */
@HiltAndroidApp
class SixSenseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupWorkManagerOnAppStart(this)
    }
}