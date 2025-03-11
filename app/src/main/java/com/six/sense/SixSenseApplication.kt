package com.six.sense

import android.app.Application
import com.six.sense.presentation.screen.worker.setupWorkManagerOnAppStart
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