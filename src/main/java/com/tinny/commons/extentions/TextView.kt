package com.tinny.commons.extentions

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

fun TextView.textValue() = this.text.toString()

fun TextView.textInt():Int = this.text.toString().toInt()


fun TextView.setFont(context: Context, font: String, fileName: String) {
    this.typeface = Typeface.createFromAsset(context.assets, fileName)
    this.text = font
}

fun TextView.setFontWithColor(context: Context, font: String, fileName: String,color:Int) {
    this.typeface = Typeface.createFromAsset(context.assets, fileName)
    this.text = font
    this.setTextColor(color)
}
