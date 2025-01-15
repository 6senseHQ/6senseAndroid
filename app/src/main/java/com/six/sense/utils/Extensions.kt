package com.six.sense.utils

import android.util.Log

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