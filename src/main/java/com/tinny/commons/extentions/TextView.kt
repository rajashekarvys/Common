package com.tinny.commons.extentions

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

fun TextView.textValue() = this.text.toString()

fun TextView.setFont(context: Context, font: String, fileName: String) {
    this.typeface = Typeface.createFromAsset(context.assets, fileName)
    this.text = font
}