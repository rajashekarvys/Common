package com.tinny.commons.views

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class MySquareRelativeLayout : RelativeLayout {
    var isHorizontalScrolling = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val spec = if (isHorizontalScrolling) heightMeasureSpec else widthMeasureSpec
        super.onMeasure(spec, spec)
    }
}