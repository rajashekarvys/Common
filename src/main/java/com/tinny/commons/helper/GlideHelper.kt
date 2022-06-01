package com.tinny.commons.helper

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

object GlideHelper {


    fun showGif(activity: AppCompatActivity,image:Int,imageView: ImageView,count:Int=1){
        Glide.with(activity)
            .load(image)
            .into(GifDrawableImageViewTarget(imageView,count))
    }
}