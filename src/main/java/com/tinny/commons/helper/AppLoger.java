package com.tinny.commons.helper;

import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class use to create the application logs
 */

public class AppLoger {
    public static void debugLogs(String TAG, String message) {
//        Log.d(TAG, message);
    }

    public static void errorLogs(String TAG, String message) {
        Log.e(TAG, message);

    }


}
