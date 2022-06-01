package com.tinny.commons.helper

import android.R
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget

 class GifDrawableImageViewTarget(view: ImageView, loopCount: Int) :
    ImageViewTarget<Drawable>(view) {
    private var mLoopCount = loopCount

    override fun setResource(resource: Drawable?) {
        if (resource is GifDrawable) {
            resource.setLoopCount(mLoopCount)
            view.setImageDrawable(resource)
        }
    }

}