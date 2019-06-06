package com.tinny.commons.views

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import java.util.*

class QuiltView : FrameLayout, ViewTreeObserver.OnGlobalLayoutListener {

    lateinit var quilt: QuiltViewBase
    lateinit var scroll: ViewGroup
    var padding = 5
    var isVertical = false
    lateinit var views: ArrayList<View>
    private var adapter: Adapter? = null

    private val adapterObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            onDataChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            onDataChanged()
        }

        fun onDataChanged() {
            setViewsFromAdapter(adapter!!)
        }
    }

    constructor(context: Context, isVertical: Boolean) : super(context) {
        this.isVertical = isVertical
        setup()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        /* TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.QuiltView);

        String orientation = a.getString(R.styleable.QuiltView_scrollOrientation);
        if(orientation != null){
            if(orientation.equals("vertical")){
                isVertical = true;
            } else {
                isVertical = false;
            }
        }*/
        isVertical = true
        setup()
    }

    fun setup() {
        views = ArrayList()

        if (isVertical) {
            scroll = ScrollView(this.context)
        } else {
            scroll = HorizontalScrollView(this.context)
        }
        quilt = QuiltViewBase(context, isVertical)
        scroll.addView(quilt)
        this.addView(scroll)

    }

    fun setAdapter(adapter: Adapter) {
        this.adapter = adapter
        adapter.registerDataSetObserver(adapterObserver)
        setViewsFromAdapter(adapter)
    }

    private fun setViewsFromAdapter(adapter: Adapter) {
        this.removeAllViews()
        for (i in 0 until adapter.count) {
            quilt.addPatch(adapter.getView(i, null, quilt))
        }
    }

    fun addPatchImages(images: ArrayList<ImageView>) {

        for (image in images) {
            val params =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            image.layoutParams = params

            val wrapper = LinearLayout(this.context)
            wrapper.setPadding(padding, padding, padding, padding)
            wrapper.addView(image)
            quilt.addPatch(wrapper)
        }
    }

    fun addPatchViews(views_a: ArrayList<View>) {
        for (view in views_a) {
            quilt.addPatch(view)
        }
    }

    fun addPatchesOnLayout() {
        for (view in views) {
            quilt.addPatch(view)
        }
    }

    fun removeQuilt(view: View) {
        quilt.removeView(view)
    }

    fun setChildPadding(padding: Int) {
        this.padding = padding
    }

    fun refresh() {
        quilt.refresh()
    }

    fun setOrientation(isVertical: Boolean) {
        this.isVertical = isVertical
    }


    override fun onGlobalLayout() {
        //addPatchesOnLayout();
    }
}





