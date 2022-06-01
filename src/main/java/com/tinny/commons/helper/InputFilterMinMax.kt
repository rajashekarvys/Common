package com.tinny.commons.helper

import android.text.InputFilter
import android.text.Spanned


class InputFilterMinMax : InputFilter {
    private var min: Int = 0
    private var max: Int = 0

    constructor(min: Int, max: Int) {
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String) {
        this.min = Integer.parseInt(min)
        this.max = Integer.parseInt(max)
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dStart: Int,
        dEnd: Int
    ): CharSequence? {
        try {
            // Remove the string out of destination that is to be replaced
            var newVal = dest.toString().substring(0, dStart) + dest.toString().substring(dEnd, dest.toString().length)
            newVal = newVal.substring(0, dStart) + source.toString() + newVal.substring(dStart, newVal.length)

            if (isInRange(min, max, newVal.toInt())) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        return ""
    }


    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}