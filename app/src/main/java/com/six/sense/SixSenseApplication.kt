package com.six.sense

import android.app.Application
import com.facebook.FacebookSdk
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application class for the 6Sense app.
 */
@HiltAndroidApp
class SixSenseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any necessary libraries or components here
    }

}