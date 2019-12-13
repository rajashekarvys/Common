package com.tinny.commons.helper;


import android.util.Log;


/**
 * This class use to create the application logs
 */


public class AppLogger {
    public static void debugLogs(String TAG, String message) {
        Log.d(TAG, message);
    }

    public static void errorLogs(String TAG, String message) {
        Log.e(TAG, message);

    }


}
