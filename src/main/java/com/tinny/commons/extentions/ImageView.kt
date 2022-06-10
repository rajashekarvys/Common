package com.tinny.commons.extentions

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView

 fun ImageView.getBitmap(): Bitmap {
    return (drawable as BitmapDrawable).bitmap
}