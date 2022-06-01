package com.tinny.commons.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.tinny.commons.R
import kotlin.math.cos
import kotlin.math.sin

class HexagonProgress : View {
    private lateinit var hexagonPath: Path
    private lateinit var hexagonBorderPath: Path
    private lateinit var partialPath: Path

    private var radius = 0f
    private var viewWidth = 0
    private var viewHeight = 0
    private lateinit var paint: Paint
    private lateinit var paintBorder: Paint
    private lateinit var paintProgress: Paint
    private lateinit var textPaint: Paint
    private var centerText = "0"
    private var progress = 0f
    private var textColor = 0
    private var textSize = 0f
    private var fillColor = 0
    private var borderColor = 0
    private var progressColor = 0
    private var strokeWidth = 0f
    private var maxProgress = 0f

    var centerX = 0f
    var centerY = 0f

    constructor(context: Context?) : super(context) {
        setup()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
        setup()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
        setup()
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        val styles =
            context!!.theme.obtainStyledAttributes(attrs, R.styleable.HexagonProgress, 0, 0)
        progress = styles.getFloat(R.styleable.HexagonProgress_progress, 0f)
        textColor = styles.getColor(R.styleable.HexagonProgress_textColor, Color.WHITE)
        textSize = styles.getFloat(R.styleable.HexagonProgress_hexaTextSize, 20f)
        maxProgress =styles.getFloat(R.styleable.HexagonProgress_maxProgress,100f)
        if (styles.getString(R.styleable.HexagonProgress_text) != null) {
            centerText = styles.getString(R.styleable.HexagonProgress_text)!!
        }

        strokeWidth = styles.getFloat(R.styleable.HexagonProgress_strokeWidthCommon, 15f)
        radius = styles.getFloat(R.styleable.HexagonProgress_borderRadius, 20f)
        borderColor = styles.getColor(R.styleable.HexagonProgress_borderColor, Color.DKGRAY)
        progressColor = styles.getColor(R.styleable.HexagonProgress_HexaProgressColor, Color.GREEN)
        fillColor = styles.getColor(R.styleable.HexagonProgress_fillColor, Color.BLUE)
    }


    private fun setup() {

        paint = Paint()
//        paint.isAntiAlias = true
        paint.flags =Paint.ANTI_ALIAS_FLAG
        val corEffect = CornerPathEffect(20f)
        paint.pathEffect = corEffect
        paint.color = Color.BLUE


        paintBorder = Paint()
        paintBorder.flags =Paint.ANTI_ALIAS_FLAG

        setLayerType(LAYER_TYPE_SOFTWARE, paintBorder)
        paintBorder.style = Paint.Style.STROKE
        paintBorder.setShadowLayer(4.0f, 1.0f, 1.0f, Color.WHITE)
        paintBorder.strokeJoin = Paint.Join.ROUND
        paintBorder.strokeCap = Paint.Cap.ROUND
        paintBorder.color = borderColor
        paintBorder.strokeWidth = strokeWidth
        paintBorder.pathEffect = corEffect
        paintProgress = Paint()
        paintProgress.color = progressColor
        paintProgress.flags =Paint.ANTI_ALIAS_FLAG
        paintProgress.strokeWidth = strokeWidth - 2
        paintProgress.style = Paint.Style.STROKE
        paintProgress.strokeCap = Paint.Cap.ROUND
        paintProgress.pathEffect = corEffect

        textPaint = Paint()
        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        hexagonPath = Path()
        hexagonBorderPath = Path()
    }

    fun fillColor(color: Int) {
        this.fillColor = color
        paint.color = fillColor
        invalidate()
    }

    fun borderColor(color: Int) {
        this.borderColor = color
        paintBorder.color = borderColor
        invalidate()
    }

    fun setProgressColor(color: Int) {
        this.progressColor = color
        paintProgress.color = progressColor
        invalidate()
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        paintBorder.strokeWidth = strokeWidth
        paintProgress.strokeWidth = strokeWidth
        this.invalidate()
    }

    fun setCornerRaduis(radius: Float) {
        this.radius = radius
        val corEffect = CornerPathEffect(radius)
        paintBorder.pathEffect = corEffect
        paintProgress.pathEffect = corEffect
        paintBorder.pathEffect = corEffect
        invalidate()
    }

    fun setText(text: String) {
        this.centerText = text
        centerText = centerText
        invalidate()
    }

    fun setTextColor(color: Int) {
        this.textColor = color
        textPaint.color = textColor
        invalidate()
    }

    fun setMaxProgress(maxProgress:Float){
        this.maxProgress = maxProgress
    }

    fun setTextSize(size: Float) {
        this.textSize = size
        textPaint.textSize = textSize
        invalidate()
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        val measure = PathMeasure(hexagonBorderPath, false)
        val length = measure.length
        partialPath = Path()
        measure.getSegment(0.0f, length * progress / maxProgress, partialPath, true)
        partialPath!!.rLineTo(
            0.0f,
            0.0f
        ) // workaround to display on hardware accelerated canvas as described in docs
    }


    private fun calculatePath() {
        centerX = viewWidth / 2.toFloat()
        centerY = viewHeight / 2.toFloat()
        hexagonBorderPath = createHexagon(radius, centerX, centerY)
        hexagonPath = createHexagon(radius - 15, centerX, centerY)
        setProgress(progress)
    }

    private fun createHexagon(
        size: Float,
        centerX: Float,
        centerY: Float
    ): Path {
        val section = (2.0 * Math.PI / 6).toFloat()
        val radius = size.toInt()
        val hex = Path()
        hex.reset()
        hex.moveTo((centerY + radius * sin(0.0)).toFloat(), (centerX + radius * cos(0.0)).toFloat())
        Log.d(
            "Test===",
            "x ==== moveTo" + centerX + radius * cos(0.0) + "y ==== moveTo" + centerX + radius * cos(
                0.0
            )
        )
        for (i in 1..5) {

//            Actual
            /*   val x = (centerX + radius * cos(section * i.toDouble())).toFloat()
               val y = (centerY + radius * sin(section * i.toDouble())).toFloat()*/

//            TODO Testing
            val x = (centerX + radius * sin(section * i.toDouble())).toFloat()
            val y = (centerY + radius * cos(section * i.toDouble())).toFloat()
            Log.d("Test===", "x ==== " + x + "y ==== " + y)
            hex.lineTo(x, y)
        }
        hex.close()
        return hex
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DrawAllocation")
    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawPath(hexagonBorderPath, paintBorder)
        canvas.drawPath(hexagonPath, paint)
        //        canvas.drawPath(hexagonPath, paint);
        if (progress > 0) {
            canvas.drawPath(partialPath, paintProgress)
        }

        if (!TextUtils.isEmpty("Hello")) {
            canvas.drawText(centerText, centerX, centerY, textPaint)
        }
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
      /*  viewWidth = (width - strokeWidth * 2).toInt()
        viewHeight = (height - strokeWidth * 2).toInt()*/
        viewWidth = width
        viewHeight = height
        radius = height / 2 - strokeWidth
        calculatePath()
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            viewWidth
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        result = if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            viewHeight
        }
        return result
    }
}