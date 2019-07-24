package com.tinny.commons.extentions

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan


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

fun TextView.changeWordColor(fullText: String, word: String, color: Int) {
    val builder = SpannableStringBuilder()
    val startIndex = fullText.indexOf(word.toLowerCase().trim())
    val endIndex = startIndex + word.toLowerCase().trim().length
    val spannableString = SpannableString(fullText)
    spannableString.setSpan(
        ForegroundColorSpan(color),
        startIndex,
        endIndex,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    ) //To change color of text
    builder.append(spannableString)
    this.setText(builder, TextView.BufferType.SPANNABLE)
}

fun TextView.chnageWordToBold(word: String) {
    val builder = SpannableStringBuilder()
    val startIndex = text.indexOf(word.toLowerCase().trim())
    val endIndex = startIndex + word.toLowerCase().trim().length
    val spannableString = SpannableString(text)
    val boldSpan = StyleSpan(Typeface.BOLD)
    spannableString.setSpan(boldSpan, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE) //To make text Bold
    builder.append(spannableString)
    setText(builder, TextView.BufferType.SPANNABLE)
}

fun TextView.changeWordSize(word: String) {
    val builder = SpannableStringBuilder()
    val startIndex = text.indexOf(word.toLowerCase().trim())
    val endIndex = startIndex + word.toLowerCase().trim().length
    val spannableString = SpannableString(text)
    spannableString.setSpan(RelativeSizeSpan(1.5f), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    builder.append(spannableString)
    setText(builder, TextView.BufferType.SPANNABLE)
}
