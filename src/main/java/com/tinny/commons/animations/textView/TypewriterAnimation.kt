package com.tinny.commons.animations.textView

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.TextView


class TypewriterAnimation : TextView {

    private var mText: CharSequence? = null
    private var mIndex: Int = 0
    private var mDelay: Long = 500 //Default 500ms delay

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private var mHandler: Handler = Handler()
    private val characterAdder = object : Runnable {
        override fun run() {
            text = mText!!.subSequence(0, mIndex++)
            if (mIndex <= mText!!.length) {
                mHandler.postDelayed(this, mDelay)
            }
        }
    }

    fun setAnimText(text: String) {
        mText = text
        mIndex = 0

        setText("")
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }


    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }
}