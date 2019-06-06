package com.tinny.commons.views

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import java.util.*

class QuiltViewBase(context: Context, isVertical: Boolean) : GridLayout(context) {

    lateinit var size: IntArray
    var columns: Int = 0
    var rows: Int = 0
    var view_width = -1
    var view_height = -1
    var isVertical = true
    var views: ArrayList<View>

    // width
    // height
    val baseSize: IntArray
        get() {
            val size = IntArray(2)

            val width_height_ratio = 3.0f / 4.0f

            val base_width = baseWidth
            val base_height = (base_width * width_height_ratio).toInt()

            size[0] = base_width
            size[1] = base_height
            return size
        }

    // width
    // height
    val baseSizeVertical: IntArray
        get() {
            val size = IntArray(2)

            val width_height_ratio = 3.0f / 4.0f

            val base_width = baseWidth
            val base_height = (base_width * width_height_ratio).toInt()

            size[0] = base_width
            size[1] = base_height
            return size
        }

    // width
    // height
    val baseSizeHorizontal: IntArray
        get() {
            val size = IntArray(2)

            val width_height_ratio = 4.0f / 3.0f

            val base_height = baseHeight
            val base_width = (base_height * width_height_ratio).toInt()

            size[0] = base_width
            size[1] = base_height
            return size
        }

    val baseWidth: Int
        get() {
            if (view_width < 500) {
                columns = 2
            } else if (view_width < 801) {
                columns = 3
            } else if (view_width < 1201) {
                columns = 4
            } else if (view_width < 1601) {
                columns = 5
            } else {
                columns = 6
            }
            return view_width / columns
        }

    val baseHeight: Int
        get() {
            if (view_height < 350) {
                rows = 2
            } else if (view_height < 650) {
                rows = 3
            } else if (view_height < 1050) {
                rows = 4
            } else if (view_height < 1250) {
                rows = 5
            } else {
                rows = 6
            }
            return view_height / rows
        }

    init {
        this.isVertical = isVertical
        if (view_width == -1) {
            val metrics = this.resources.displayMetrics
            val width = metrics.widthPixels
            val height = metrics.heightPixels - 120
            view_width = width - this.paddingLeft - this.paddingRight
            view_height = height - this.paddingTop - this.paddingBottom
        }
        views = ArrayList()
        setup()
    }

    fun setup() {
        if (isVertical) {
            setupVertical()
        } else {
            setupHorizontal()
        }
    }

    fun setupVertical() {
        size = baseSizeVertical
        this.columnCount = columns
        this.rowCount = -1
        this.orientation = HORIZONTAL
        val params =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        this.layoutParams = params
    }

    fun setupHorizontal() {
        size = baseSizeHorizontal
        this.rowCount = rows
        this.columnCount = -1
        this.orientation = VERTICAL
        val params =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        this.layoutParams = params
    }

    fun addPatch(view: View) {

        val count = this.childCount

        val child = QuiltViewPatch.init(count, columns)

        val params = GridLayout.LayoutParams()
        params.width = size[0] * child.width_ratio
        params.height = size[1] * child.height_ratio
        params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, child.height_ratio)
        params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, child.width_ratio)
        view.layoutParams = params
        addView(view)
        views.add(view)
    }

    fun refresh() {
        this.removeAllViewsInLayout()
        setup()
        for (view in views) {
            addPatch(view)
        }
    }

    /*@Override
         protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            view_width = parentWidth;
            view_height = parentHeight;

            setup(isVertical);
         }*/

    override fun onSizeChanged(xNew: Int, yNew: Int, xOld: Int, yOld: Int) {
        super.onSizeChanged(xNew, yNew, xOld, yOld)
        view_width = xNew
        view_height = yNew
    }


}