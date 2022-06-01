package com.tinny.commons.extentions

import android.database.Cursor

fun Cursor.getStringValue(key:String) = getString(getColumnIndex(key))

fun Cursor.getLongValue(key: String) = getLong(getColumnIndex(key))

fun Cursor.getIntValue(key: String) = getInt(getColumnIndex(key))