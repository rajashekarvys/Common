package com.tinny.commons.helper

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.graphics.drawable.DrawableCompat
import java.io.IOException
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


object UtilsHelper {


    fun getRandomColorWithAlpha(alpha: Int): Int {
        val r: Int = getRandomInt() % 256
        val g: Int = getRandomInt() % 256
        val b: Int = getRandomInt() % 256
        return Color.argb(alpha, r, g, b)
    }

    fun removeAlphaFromColor(oldColor:Int):Int{
       return oldColor or -0x1000000
    }

    fun mapValueFromRangeToRange(value: Float, fromLow: Float, fromHigh: Float, toLow: Float, toHigh: Float): Float {
        return toLow + (value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow)
    }

    fun clamp(value: Float, low: Float, high: Float): Float {
        return Math.min(Math.max(value, low), high)
    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun distanceBetweenXYPoints(x:Double,y:Double,x1:Double,y1:Double):Double{
        return sqrt((x1 - x).pow(2.0) + (y1 - y).pow(2.0))

    }
    fun getGerdientBackGround(colors: IntArray?): GradientDrawable? {
        val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
        gd.shape = GradientDrawable.RECTANGLE
        return gd
    }

    fun getGerdientBackGroundRoundRect(colors: IntArray?): GradientDrawable? {
        val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT
        gd.shape = GradientDrawable.RECTANGLE
        gd.cornerRadius = 25f
        return gd
    }

    fun changeBackColor(drawable: Drawable, colors: Int) {
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, colors)
    }

    fun changeDrawbleColor(drawable: Drawable, color: Int) {
        drawable.mutate()
        drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }



    fun getEmojiByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    fun getRandomInt(): Int {
        return abs(Random().nextInt())
    }

    /**
     * @param min minimum value
     * @param max maximum value
     * @param random Random object
     * @return int from given range of numbers
     * */
    fun randInt(min: Int, max: Int, random: Random = Random()): Int {
        return random.nextInt(max - min + 1) + min
    }

    /**
     * @param min minimum value
     * @param max maximum value
     * @param random Random object
     * @param random List of Data
     * @return int from given range of numbers
     * */
    fun randIntUnique(min: Int, max: Int, random: Random, list: ArrayList<Int>): Int {
        var number = randInt(min, max, random)
        while (list.contains(number)) {
            number = randInt(min, max, random)
        }
        AppLogger.debugLogs("Test", "randIntUnique $number")
        return number
    }

    fun randIntOdd(min: Int, max: Int): Int {
        return min + Random().nextInt((max - min) / 2) * 2 + 1
    }

    fun randIntEven(min: Int, max: Int): Int {
        return min + Random().nextInt((max - min) / 2) * 2
    }

    fun readFileFromAssets(context: Context,fileName:String): String {
        val json: String
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.use { it.read(buffer) }
            json = String(buffer)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return ""
        }

        return json

    }


    fun getScreenDimensions(context: Context): Pair<Int,Int> {
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.displayMetrics.heightPixels
        return Pair(width, height)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting!!
    }
}