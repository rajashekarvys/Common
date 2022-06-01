package com.tinny.commons.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.tinny.commons.helper.AppLogger
import com.tinny.commons.helper.UtilsHelper
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Draw line from a point to another point
 *
 * linesToDraw list with pairs x,y and x1,y1
 * */
class DrawDifferentLines : View {

    private lateinit var paint: Paint
    private lateinit var textPaint: Paint
    var linesToDraw: ArrayList<Pair<Pair<Float, Float>, Pair<Float, Float>>>? = null
        get() = field
        set(value) {
            field = value
        }

    private fun setUp() {
        paint = Paint()
//        paint.isAntiAlias = true
        paint.flags = Paint.ANTI_ALIAS_FLAG
        val corEffect = CornerPathEffect(30f)
        paint.pathEffect = corEffect
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLUE

        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 50f
        textPaint.isAntiAlias = true
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textAlign = Paint.Align.CENTER
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setUp()

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setUp()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (linesToDraw != null) {
            for (i in linesToDraw!!) {

//                canvas!!.drawLine(i.first.first, i.first.second, i.second.first, i.second.second, paint)
                /*  drawArrow1(canvas!!, i.first, i.second, doorXY!!.first, doorXY!!.second, paint)
                  drawArrow(canvas!!, i.first, i.second, doorXY!!.first, doorXY!!.second, paint)*/
                val path = Path()
                path.moveTo(i.first.first + 10, i.first.second)
                path.lineTo(i.second.first + 10, i.second.second)

                val distance = sqrt((i.first.first - i.second.first.toDouble()).pow(2.0) + (i.first.second - i.second.second.toDouble()).pow(2.0))
                val dp = ((UtilsHelper.dpFromPx(context, distance.toFloat())) / 6.299)
                val mm = String.format("%.1f", dp)
                AppLogger.debugLogs(TAG = "hello", message = "distance $distance distance in mm $dp ")
                canvas!!.drawTextOnPath(" $mm mm", path, 10f, 10f, textPaint)

            }

        }
    }
}