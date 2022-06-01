package com.tinny.commons.animations

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Property
import android.view.View
import androidx.annotation.RequiresApi

import com.tinny.commons.helper.UtilsHelper

class DotsView : View {

    private val circlePaints = arrayOfNulls<Paint>(4)

    private val DOTS_COUNT = 7
    private val OUTER_DOTS_POSITION_ANGLE = 360 / DOTS_COUNT

    private val COLOR_1 = Color.GREEN
    private val COLOR_2 = Color.BLUE
    private val COLOR_3 = Color.RED
    private val COLOR_4 = Color.YELLOW
    private var centerX: Int = 0
    private var centerY: Int = 0

    private var maxOuterDotsRadius: Float = 0f
    private var maxInnerDotsRadius: Float = 0f
    private var maxDotSize: Float = 0f

    var currentProgress = 0f

        set(currentProgress) {
            field = currentProgress

            updateInnerDotsPosition()
            updateOuterDotsPosition()
            updateDotsPaints()
            updateDotsAlpha()

            postInvalidate()
        }

    private var currentRadius1 = 0f
    private var currentDotSize1 = 0f

    private var currentDotSize2 = 0f
    private var currentRadius2 = 0f

    private val argbEvaluator = ArgbEvaluator()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        for (i in circlePaints.indices) {
            circlePaints[i] = Paint()
            circlePaints[i]!!.style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        maxDotSize = 20f
        maxOuterDotsRadius = w / 2 - maxDotSize * 2
        maxInnerDotsRadius = 0.8f * maxOuterDotsRadius
    }

    override fun onDraw(canvas: Canvas) {
        drawOuterDotsFrame(canvas)
        drawInnerDotsFrame(canvas)
    }

    private fun drawOuterDotsFrame(canvas: Canvas) {
        for (i in 0 until DOTS_COUNT) {
            val cX = (centerX + currentRadius1 * Math.cos(i.toDouble() * OUTER_DOTS_POSITION_ANGLE.toDouble() * Math.PI / 180)).toInt()
            val cY = (centerY + currentRadius1 * Math.sin(i.toDouble() * OUTER_DOTS_POSITION_ANGLE.toDouble() * Math.PI / 180)).toInt()
            canvas.drawCircle(cX.toFloat(), cY.toFloat(), currentDotSize1, circlePaints[i % circlePaints.size]!!)
        }
    }

    private fun drawInnerDotsFrame(canvas: Canvas) {
        for (i in 0 until DOTS_COUNT) {
            val cX = (centerX + currentRadius2 * Math.cos((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            val cY = (centerY + currentRadius2 * Math.sin((i * OUTER_DOTS_POSITION_ANGLE - 10) * Math.PI / 180)).toInt()
            canvas.drawCircle(cX.toFloat(), cY.toFloat(), currentDotSize2, circlePaints[(i + 1) % circlePaints.size]!!)
        }
    }

    private fun updateInnerDotsPosition() {
        if (this.currentProgress < 0.3f) {
            this.currentRadius2 = UtilsHelper.mapValueFromRangeToRange(currentProgress, 0.0f, 0.3f, 0.0f, maxInnerDotsRadius)
        } else {
            this.currentRadius2 = maxInnerDotsRadius
        }

        if (this.currentProgress < 0.2) {
            this.currentDotSize2 = maxDotSize
        } else if (this.currentProgress < 0.5) {
            this.currentDotSize2 = UtilsHelper.mapValueFromRangeToRange(currentProgress, 0.2f, 0.5f, maxDotSize, 0.3f * maxDotSize)
        } else {
            this.currentDotSize2 = UtilsHelper.mapValueFromRangeToRange(currentProgress, 0.5f, 1.0f, (maxDotSize * 0.3f), 0.0f)
        }

    }

    private fun updateOuterDotsPosition() {
        if (this.currentProgress < 0.3f) {
            this.currentRadius1 = UtilsHelper.mapValueFromRangeToRange(currentProgress, 0.0f, 0.3f, 0.0f, (maxOuterDotsRadius * 0.8f))
        } else {
            this.currentRadius1 = UtilsHelper.mapValueFromRangeToRange(currentProgress, 0.3f, 1.0f, (0.8f * maxOuterDotsRadius), maxOuterDotsRadius)
        }

        if (this.currentProgress < 0.7) {
            this.currentDotSize1 = maxDotSize
        } else {
            this.currentDotSize1 = UtilsHelper.mapValueFromRangeToRange(currentProgress, 0.7f, 1.0f, maxDotSize, 0f)
        }
    }

    private fun updateDotsPaints() {
        if (this.currentProgress < 0.5f) {
            val progress = UtilsHelper.mapValueFromRangeToRange(this.currentProgress, 0f, 0.5f, 0f, 1f) as Float
            circlePaints[0]!!.color = argbEvaluator.evaluate(progress, COLOR_1, COLOR_2) as Int
            circlePaints[1]!!.color = argbEvaluator.evaluate(progress, COLOR_2, COLOR_3) as Int
            circlePaints[2]!!.color = argbEvaluator.evaluate(progress, COLOR_3, COLOR_4) as Int
            circlePaints[3]!!.color = argbEvaluator.evaluate(progress, COLOR_4, COLOR_1) as Int
        } else {
            val progress = UtilsHelper.mapValueFromRangeToRange(this.currentProgress, 0.5f, 1f, 0f, 1f) as Float
            circlePaints[0]!!.color = argbEvaluator.evaluate(progress, COLOR_2, COLOR_3) as Int
            circlePaints[1]!!.color = argbEvaluator.evaluate(progress, COLOR_3, COLOR_4) as Int
            circlePaints[2]!!.color = argbEvaluator.evaluate(progress, COLOR_4, COLOR_1) as Int
            circlePaints[3]!!.color = argbEvaluator.evaluate(progress, COLOR_1, COLOR_2) as Int
        }
    }

    private fun updateDotsAlpha() {
        val progress = UtilsHelper.clamp(currentProgress, 0.6f, 1.0f)
        val alpha = UtilsHelper.mapValueFromRangeToRange(progress, 0.6f, 1.0f, 255.0f, 0.0f).toInt()
        circlePaints[0]!!.alpha = alpha
        circlePaints[1]!!.alpha = alpha
        circlePaints[2]!!.alpha = alpha
        circlePaints[3]!!.alpha = alpha
    }

    var DOTS_PROGRESS: Property<DotsView, Float> = object : Property<DotsView, Float>(Float::class.java, "dotsProgress") {
        override fun get(dots: DotsView): Float {
            return dots.currentProgress
        }

        override fun set(dots: DotsView, value: Float?) {
            dots.currentProgress = (value!!)
        }
    }


/*
    var DOTS_PROGRESS: Property<DotsView, Float> = object : Property<DotsView, Float>(Float::class.java, "dotsProgress") {
        override fun get(dot: DotsView): Float {
            return dot.currentProgress
        }

        override fun set(`object`: DotsView, value: Float?) {
            `object`.currentProgress = (value!!)
        }
    }*/
}
