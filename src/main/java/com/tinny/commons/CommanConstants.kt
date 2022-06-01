package com.tinny.commons

import android.os.Looper
val photoExtensions: Array<String> get() = arrayOf("jpg", "png", "jpeg", "bmp", "webp","gif")
val videoExtensions: Array<String> get() = arrayOf("mp4", "mkv", "webm", "avi", "3gp", "mov", "m4v", "3gpp")
val audioExtensions: Array<String> get() = arrayOf("mp3", "wav", "wma", "ogg", "m4a", "opus", "flac", "aac")
val rawExtensions: Array<String> get() = arrayOf("dng", "orf", "nef", "arw")
// sorting
const val SORT_ORDER = "sort_order"
const val SORT_BY_NAME = 1
const val SORT_BY_DATE_MODIFIED = 2
const val SORT_BY_SIZE = 3
const val SORT_BY_DATE_TAKEN = 4
const val SORT_BY_PATH = 5
const val SORT_BY_DURATION = 6
const val REQUEST_SET_AS = 62878
const val GRID_VIEW = 0
const val LIST_VIEW = 1
const val TYPE_IMAGES = 1
const val TYPE_VIDEOS = 2

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

const val Intent_AppName = "AN"
const val Intent_Developer = "DB"

const val Intent_Translation = "Trans"

const val Intent_AppVesion = "AV"
const val Test = "aa"

const val Intent_AppIcon = "AI"
const val Intent_AppPACKAGENAME = "APN"