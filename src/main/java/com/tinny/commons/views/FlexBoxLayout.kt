package com.tinny.commons.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.tinny.commons.R

/**
 * FlexBoxLayout places content horizontally and wraps vertically when there is no horizontal space
 */
class FlexBoxLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var mXSpacing = 0
        get() = field
        set(value) {
            field = value
        }
    private var mYSpacing = 0
        get() = field
        set(value) {
            field = value
        }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.getSize(heightMeasureSpec)
        // final width and height
        var measuredWidth = 0

        var measuredHeight = 0
        // current maximum width and height in iteration
        var currMaxWidth = 0
        var currMaxHeight = 0
        val availWidth = widthSpec - paddingLeft - paddingRight - mXSpacing - mXSpacing
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val cWidth = child.measuredWidth + mXSpacing
            val cHeight = child.measuredHeight + mYSpacing
            currMaxWidth += cWidth
            if (currMaxWidth > availWidth) {
                measuredWidth = measuredWidth.coerceAtLeast((currMaxWidth - cWidth).coerceAtLeast(cWidth))
                measuredHeight += currMaxHeight
                currMaxWidth = cWidth
                currMaxHeight = 0
            }
            if (i == count - 1) {
                measuredHeight += currMaxHeight.coerceAtLeast(cHeight)
            }
            currMaxHeight = currMaxHeight.coerceAtLeast(cHeight)
        }
        //        measuredWidth += mXSpacing;
        measuredHeight += mYSpacing
        measuredWidth += paddingLeft + paddingRight
        measuredHeight += paddingTop + paddingBottom
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) measuredWidth = widthSpec else if (widthMeasureSpec == MeasureSpec.AT_MOST) measuredWidth = Math.min(measuredWidth, widthSpec)
        if (heightMode == MeasureSpec.EXACTLY) measuredHeight = heightSpec else if (heightMode == MeasureSpec.AT_MOST) measuredHeight = Math.min(measuredHeight, heightSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childLeft = paddingLeft + mXSpacing
        val childTop = paddingTop + mYSpacing
        val childRight = width - paddingRight - mXSpacing
        val childBottom = height - paddingBottom - mYSpacing
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop
        var currLeft = childLeft
        var currTop = childTop
        var currMaxHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (currLeft + child.measuredWidth > childWidth) {
                currLeft = childLeft
                currTop += currMaxHeight + mYSpacing
                currMaxHeight = 0
                if (currTop > childHeight) break
            }
            child.layout(
                currLeft,
                currTop,
                Math.min(currLeft + child.measuredWidth, childRight),
                Math.min(currTop + child.measuredHeight, childBottom)
            )
            currLeft += child.measuredWidth + mXSpacing
            currMaxHeight = Math.max(currMaxHeight, child.measuredHeight)
        }
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mXSpacing = 15
        mYSpacing = 15
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FlexBoxLayout, 0, 0)
            mXSpacing = a.getDimensionPixelSize(R.styleable.FlexBoxLayout_horizontalSpacing, mXSpacing)
            mYSpacing = a.getDimensionPixelSize(R.styleable.FlexBoxLayout_verticalSpacing, mYSpacing)
            a.recycle()
        }
    }

    init {
        init(context, attrs)
    }
}