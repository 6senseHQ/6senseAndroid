package com.six.sense.utils

import android.util.Log


fun Any?.log(tag: String = "TAG"): Any? {
    Log.d("com.six.sense.utils.log> '$tag'", "$tag - $this : ${this?.javaClass?.name?.split('.')?.lastOrNull() ?: ""}")
    return this
}