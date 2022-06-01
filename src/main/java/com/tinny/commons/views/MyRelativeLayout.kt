package com.tinny.commons.views

import android.R
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.tinny.commons.helper.AppLogger
import com.tinny.commons.helper.UtilsHelper
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Draw line from a point to different places like rays
 * doorXY is point
 * linesToDraw is lines from doorXY
 * */
class MyRelativeLayout : View {
    private lateinit var paint: Paint
    private lateinit var textPaint: Paint
    private lateinit var contextA: Context

    var linesToDraw: ArrayList<Pair<Float, Float>>? = null
        get() = field
        set(value) {
            field = value
        }

    var doorXY: Pair<Float, Float>? = null
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
        textPaint.color = Color.RED
        textPaint.textSize = 50f
        textPaint.isAntiAlias = true
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textAlign = Paint.Align.CENTER
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setUp()
        this.contextA = context

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setUp()
        this.contextA = context
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (linesToDraw != null) {
            for (i in linesToDraw!!) {
                canvas!!.drawLine(i.first, i.second, doorXY!!.first, doorXY!!.second, paint)
                /*  drawArrow1(canvas!!, i.first, i.second, doorXY!!.first, doorXY!!.second, paint)
                  drawArrow(canvas!!, i.first, i.second, doorXY!!.first, doorXY!!.second, paint)*/
                val path = Path()
                path.moveTo(doorXY!!.first+10, doorXY!!.second)
                path.lineTo(i.first+10, i.second)

                val distance = sqrt(Math.pow(doorXY!!.first - i.first.toDouble(), 2.0) + Math.pow(doorXY!!.second - i.second.toDouble(), 2.0))
                val dp = ((UtilsHelper.dpFromPx(contextA, distance.toFloat())) / 6.299)
                val mm = String.format("%.1f", dp)
                AppLogger.debugLogs(TAG = "hello",message = "distance $distance distance in mm $dp ")
                canvas.drawTextOnPath(" $mm mm", path, 0f, 0f, textPaint)

            }

        }
    }


    private fun drawArrow1(canvas: Canvas, x1: Float, y1: Float, x: Float, y: Float, paint: Paint) {
        val degree = calculateDegree(x, x1, y, y1)
        val endX1 = (x1 + 10 * cos(Math.toRadians(degree - 30 + 90))).toFloat()
        val endY1 = (y1 + 10 * sin(Math.toRadians(degree - 30 + 90))).toFloat()
        val endX2 = (x1 + 10 * cos(Math.toRadians(degree - 60 + 180))).toFloat()
        val endY2 = (y1 + 10 * sin(Math.toRadians(degree - 60 + 180))).toFloat()
        canvas.drawLine(x1, y1, endX1, endY1, paint)
        canvas.drawLine(x1, y1, endX2, endY2, paint)
    }

    private fun drawArrow(canvas: Canvas, x1: Float, y1: Float, x: Float, y: Float, paint: Paint) {
        val degree1 = calculateDegree(x, x1, y, y1)
        val endX11 = (R.attr.x + 10 * cos(Math.toRadians(degree1 - 30 + 90))).toFloat()
        val endY11 = (R.attr.y + 10 * sin(Math.toRadians(degree1 - 30 + 90))).toFloat()
        val endX22 = (R.attr.x + 10 * cos(Math.toRadians(degree1 - 60 + 180))).toFloat()
        val endY22 = (R.attr.y + 10 * sin(Math.toRadians(degree1 - 60 + 180))).toFloat()
        canvas.drawLine(R.attr.x.toFloat(), R.attr.y.toFloat(), endX11, endY11, paint)
        canvas.drawLine(R.attr.x.toFloat(), R.attr.y.toFloat(), endX22, endY22, paint)
    }

    private fun calculateDegree(x1: Float, x2: Float, y1: Float, y2: Float): Double {
        var startRadians = atan((y2 - y1) / (x2 - x1).toDouble()).toFloat()
        println("radian=====" + Math.toDegrees(startRadians.toDouble()))
        /*startRadians += (if (x2 >= x1) 90 else -90)
        startRadians=(startRadians*Math.PI / 180.toFloat()).toFloat()*/

        startRadians += ((if (x2 >= x1) 90 else -90) * Math.PI / 180).toFloat()

        return Math.toDegrees(startRadians.toDouble())
    }
}