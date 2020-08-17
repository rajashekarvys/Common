package com.tinny.commons.extentions

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView


fun ImageView.getBitmap(): Bitmap {
    return (drawable as BitmapDrawable).bitmap
}

fun ImageView.getRotatedBitmap(angle:Float):Bitmap{
    val source =getBitmap()
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

fun ImageView.changeImageColor(color:Int){
    this.setColorFilter(color)
}