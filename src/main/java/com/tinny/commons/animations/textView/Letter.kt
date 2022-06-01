package com.tinny.commons.animations.textView

import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import kotlin.math.max
import kotlin.math.min

class Letter : CharacterStyle(), UpdateAppearance {

    private var alpha = 0.0f

    override fun updateDrawState(tp: TextPaint) {
        val color = (0xFF * alpha).toInt() shl 24 or (tp.color and 0x00FFFFFF)
        tp.color = color
    }

    fun setAlpha(alpha: Float) {
        this.alpha = max(min(alpha, 1.0f), 0.0f)
    }

}
