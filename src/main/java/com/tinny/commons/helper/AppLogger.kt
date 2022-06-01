package com.tinny.commons.helper

import android.util.Log

/**
 * This class use to create the application logs
 */
object AppLogger {
    fun debugLogs(TAG: String = "Test===", message: String) {
        Log.d(TAG, message)
    }

    fun errorLogs(TAG: String?, message: String?) {
        Log.e(TAG, message!!)
    }

}