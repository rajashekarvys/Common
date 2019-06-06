/*
 * Copyright 2018 L4 Digital. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tinny.commons.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tinny.commons.R

/**
 * A ListView-like FastScroller for the [RecyclerView].
 *
 *
 * FastScroller provides the fast scrolling and section indexing for a RecyclerView,
 * with a Lollipop styled scrollbar and section “bubble” view. The scrollbar provides
 * a handle for quickly navigating the list while the bubble view displays the
 * currently visible section index.
 *
 *
 * The following attributes can be set to customize the visibility and appearance of
 * the elements within the FastScroller view:
 *
 *
 * [R.styleable.FastScroller_hideScrollbar]
 * [R.styleable.FastScroller_showBubble]
 * [R.styleable.FastScroller_showTrack]
 * [R.styleable.FastScroller_handleColor]
 * [R.styleable.FastScroller_trackColor]
 * [R.styleable.FastScroller_bubbleColor]
 * [R.styleable.FastScroller_bubbleSize]
 * [R.styleable.FastScroller_bubbleTextColor]
 * [R.styleable.FastScroller_bubbleTextSize]
 */
class FastScroller : LinearLayout {

    @ColorInt
    private var bubbleColor: Int = 0
    @ColorInt
    private var handleColor: Int = 0

    private var bubbleHeight: Int = 0
    private var handleHeight: Int = 0
    private var viewHeight: Int = 0
    private var hideScrollbar: Boolean = false
    private var showBubble: Boolean = false
    private var bubbleImage: Drawable? = null
    private var handleImage: Drawable? = null
    private var trackImage: Drawable? = null
    private var handleView: ImageView? = null
    private var trackView: ImageView? = null
    private var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private var bubbleSize: Size? = null
    private var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null
    private var bubbleView: TextView? = null
    private var scrollbar: View? = null
    private var scrollbarAnimator: ViewPropertyAnimator? = null
    private var bubbleAnimator: ViewPropertyAnimator? = null

    private var fastScrollListener: FastScrollListener? = null
    private var sectionIndexer: SectionIndexer? = null

    private val scrollbarHider = Runnable { hideScrollbar() }

    private val scrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            if (!handleView!!.isSelected && isEnabled) {
                setViewPositions(getScrollProportion(recyclerView))
            }

            if (swipeRefreshLayout != null && recyclerView.layoutManager != null) {
                val firstVisibleItem = findFirstVisibleItemPosition(recyclerView.layoutManager!!)
                val topPosition = if (recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipeRefreshLayout!!.isEnabled = firstVisibleItem == 0 && topPosition >= 0
            }
        }

        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (isEnabled) {
                when (newState) {
                    androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING -> {
                        handler.removeCallbacks(scrollbarHider)
                        cancelAnimation(scrollbarAnimator)

                        if (!isViewVisible(scrollbar)) {
                            showScrollbar()
                        }
                    }

                    androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE -> if (hideScrollbar && !handleView!!.isSelected) {
                        handler.postDelayed(scrollbarHider, SCROLLBAR_HIDE_DELAY.toLong())
                    }
                }
            }
        }
    }

    enum class Size private constructor(@param:DrawableRes @field:DrawableRes var drawableId: Int, @param:DimenRes @field:DimenRes var textSizeId: Int) {
        NORMAL(R.drawable.fastscroll_bubble, R.dimen.fastscroll_bubble_text_size),
        SMALL(R.drawable.fastscroll_bubble_small, R.dimen.fastscroll_bubble_text_size_small);


        companion object {

            fun fromOrdinal(ordinal: Int): Size {
                return if (ordinal >= 0 && ordinal < values().size) values()[ordinal] else NORMAL
            }
        }
    }

    constructor(context: Context, size: Size = Size.NORMAL) : super(context) {
        layout(context, size)
        layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        layout(context, attrs)
        layoutParams = generateLayoutParams(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        layout(context, attrs)
        layoutParams = generateLayoutParams(attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < handleView!!.x - ViewCompat.getPaddingStart(scrollbar!!)) {
                    return false
                }

                requestDisallowInterceptTouchEvent(true)
                setHandleSelected(true)

                handler.removeCallbacks(scrollbarHider)
                cancelAnimation(scrollbarAnimator)
                cancelAnimation(bubbleAnimator)

                if (!isViewVisible(scrollbar)) {
                    showScrollbar()
                }

                if (showBubble && sectionIndexer != null) {
                    showBubble()
                }

                if (fastScrollListener != null) {
                    fastScrollListener!!.onFastScrollStart(this)
                }
                val y = event.y
                setViewPositions(y)
                setRecyclerViewPosition(y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                setViewPositions(y)
                setRecyclerViewPosition(y)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                requestDisallowInterceptTouchEvent(false)
                setHandleSelected(false)

                if (hideScrollbar) {
                    handler.postDelayed(scrollbarHider, SCROLLBAR_HIDE_DELAY.toLong())
                }

                hideBubble()

                if (fastScrollListener != null) {
                    fastScrollListener!!.onFastScrollStop(this)
                }

                return true
            }
        }

        return super.onTouchEvent(event)
    }

    /**
     * Set the enabled state of this view.
     *
     * @param enabled True if this view is enabled, false otherwise
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        visibility = if (enabled) View.VISIBLE else View.GONE
    }

    /**
     * Set the [ViewGroup.LayoutParams] associated with this view. These supply
     * parameters to the *parent* of this view specifying how it should be
     * arranged.
     *
     * @param params The [ViewGroup.LayoutParams] for this view, cannot be null
     */
    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT
        super.setLayoutParams(params)
    }

    /**
     * Set the [ViewGroup.LayoutParams] associated with this view. These supply
     * parameters to the *parent* of this view specifying how it should be
     * arranged.
     *
     * @param viewGroup The parent [ViewGroup] for this view, cannot be null
     */
    fun setLayoutParams(viewGroup: ViewGroup) {
        val recyclerViewId = if (recyclerView != null) recyclerView!!.id else View.NO_ID
        val marginTop = resources.getDimensionPixelSize(R.dimen.fastscroll_scrollbar_margin_top)
        val marginBottom = resources.getDimensionPixelSize(R.dimen.fastscroll_scrollbar_margin_bottom)

        if (recyclerViewId == View.NO_ID) {
            throw IllegalArgumentException("RecyclerView must have a view ID")
        }

        when (viewGroup) {
            is androidx.coordinatorlayout.widget.CoordinatorLayout -> {
                val layoutParams = layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams

                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
                layoutParams.anchorGravity = GravityCompat.END
                layoutParams.anchorId = recyclerViewId
                layoutParams.setMargins(0, marginTop, 0, marginBottom)
                setLayoutParams(layoutParams)

            }
            is FrameLayout -> {
                val layoutParams = layoutParams as FrameLayout.LayoutParams

                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
                layoutParams.gravity = GravityCompat.END
                layoutParams.setMargins(0, marginTop, 0, marginBottom)
                setLayoutParams(layoutParams)

            }
            is RelativeLayout -> {
                val layoutParams = layoutParams as RelativeLayout.LayoutParams
                val endRule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    RelativeLayout.ALIGN_END
                else
                    RelativeLayout.ALIGN_RIGHT

                layoutParams.height = 0
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, recyclerViewId)
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, recyclerViewId)
                layoutParams.addRule(endRule, recyclerViewId)
                layoutParams.setMargins(0, marginTop, 0, marginBottom)
                setLayoutParams(layoutParams)

            }
            else -> throw IllegalArgumentException("Parent ViewGroup must be a ConstraintLayout, CoordinatorLayout, FrameLayout, or RelativeLayout")
        }

        updateViewHeights()
    }

    /**
     * Set the [RecyclerView] associated with this [FastScroller]. This allows the
     * FastScroller to set its layout parameters and listen for scroll changes.
     *
     * @param recyclerView The [RecyclerView] to attach, cannot be null
     * @see .detachRecyclerView
     */
    fun attachRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        this.recyclerView = recyclerView

        /* if (getParent() instanceof ViewGroup) {
//            setLayoutParams((ViewGroup) getParent());
        } else if (recyclerView.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
            viewGroup.addView(this);
            setLayoutParams(viewGroup);
        }
*/
        recyclerView.addOnScrollListener(scrollListener)

        post {
            // set initial positions for bubble and handle
            setViewPositions(getScrollProportion(this@FastScroller.recyclerView))
        }
    }

    /**
     * Clears references to the attached [RecyclerView] and stops listening for scroll changes.
     *
     * @see .attachRecyclerView
     */
    fun detachRecyclerView() {
        if (recyclerView != null) {
            recyclerView!!.removeOnScrollListener(scrollListener)
            recyclerView = null
        }
    }

    /**
     * Set a new [FastScrollListener] that will listen to fast scroll events.
     *
     * @param fastScrollListener The new [FastScrollListener] to set, or null to set none
     */
    fun setFastScrollListener(fastScrollListener: FastScrollListener?) {
        this.fastScrollListener = fastScrollListener
    }

    /**
     * Set a new [SectionIndexer] that provides section text for this [FastScroller].
     *
     * @param sectionIndexer The new [SectionIndexer] to set, or null to set none
     */
    fun setSectionIndexer(sectionIndexer: SectionIndexer?) {
        this.sectionIndexer = sectionIndexer
    }

    /**
     * Set a [SwipeRefreshLayout] to disable when the [RecyclerView] is scrolled away from the top.
     *
     *
     * Required when [Build.VERSION.SDK_INT] < {@value Build.VERSION_CODES#LOLLIPOP}, otherwise use
     * [setNestedScrollingEnabled(true)][View.setNestedScrollingEnabled].
     *
     * @param swipeRefreshLayout The [SwipeRefreshLayout] to set, or null to set none
     */
    fun setSwipeRefreshLayout(swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout?) {
        this.swipeRefreshLayout = swipeRefreshLayout
    }

    /**
     * Hide the scrollbar when not scrolling.
     *
     * @param hideScrollbar True to hide the scrollbar, false to show
     */
    fun setHideScrollbar(hideScrollbar: Boolean) {
        this.hideScrollbar = hideScrollbar
        scrollbar!!.visibility = if (hideScrollbar) View.GONE else View.VISIBLE
    }

    /**
     * Show the scroll track while scrolling.
     *
     * @param visible True to show scroll track, false to hide
     */
    fun setTrackVisible(visible: Boolean) {
        trackView!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * Set the color of the scroll track.
     *
     * @param color The color for the scroll track
     */
    fun setTrackColor(@ColorInt color: Int) {

        if (trackImage == null) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.fastscroll_track)

            if (drawable != null) {
                trackImage = DrawableCompat.wrap(drawable)
                trackImage!!.mutate()
            }
        }

        DrawableCompat.setTint(trackImage!!, color)
        trackView!!.setImageDrawable(trackImage)
    }

    /**
     * Set the color of the scroll handle.
     *
     * @param color The color for the scroll handle
     */
    fun setHandleColor(@ColorInt color: Int) {
//       handleColor = color

        if (handleImage == null) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_datejumper)

            if (drawable != null) {
                handleImage = DrawableCompat.wrap(drawable)
                handleImage!!.mutate()
            }
        }

//        DrawableCompat.setTint(handleImage!!, handleColor)
        handleView!!.setImageDrawable(handleImage)
    }

    /**
     * Show the section bubble while scrolling.
     *
     * @param visible True to show the bubble, false to hide
     */
    fun setBubbleVisible(visible: Boolean) {
        showBubble = visible
    }

    /**
     * Set the background color of the section bubble.
     *
     * @param color The background color for the section bubble
     */
    fun setBubbleColor(@ColorInt color: Int) {
        bubbleColor = color

        if (bubbleImage == null) {
            val drawable = ContextCompat.getDrawable(context, bubbleSize!!.drawableId)

            if (drawable != null) {
                bubbleImage = DrawableCompat.wrap(drawable)
                bubbleImage!!.mutate()
            }
        }

        DrawableCompat.setTint(bubbleImage!!, bubbleColor)
        ViewCompat.setBackground(bubbleView!!, bubbleImage)
    }

    /**
     * Set the text color of the section bubble.
     *
     * @param color The text color for the section bubble
     */
    fun setBubbleTextColor(@ColorInt color: Int) {
        bubbleView!!.setTextColor(color)
    }

    /**
     * Set the scaled pixel text size of the section bubble.
     *
     * @param size The scaled pixel text size for the section bubble
     */
    fun setBubbleTextSize(size: Int) {
        bubbleView!!.textSize = size.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        viewHeight = h
    }

    private fun setRecyclerViewPosition(y: Float) {
        if (recyclerView != null && recyclerView!!.adapter != null && recyclerView!!.layoutManager != null) {
            val itemCount = recyclerView!!.adapter!!.itemCount
            val proportion: Float

            if (handleView!!.y == 0f) {
                proportion = 0f
            } else if (handleView!!.y + handleHeight >= viewHeight - TRACK_SNAP_RANGE) {
                proportion = 1f
            } else {
                proportion = y / viewHeight.toFloat()
            }

            var scrolledItemCount = Math.round(proportion * itemCount)

            if (isLayoutReversed(recyclerView!!.layoutManager!!)) {
                scrolledItemCount = itemCount - scrolledItemCount
            }

            val targetPos = getValueInRange(0, itemCount - 1, scrolledItemCount)
            recyclerView!!.layoutManager!!.scrollToPosition(targetPos)

            if (showBubble && sectionIndexer != null) {
                bubbleView!!.text = sectionIndexer!!.getSectionText(targetPos)
            }
        }
    }

    private fun getScrollProportion(recyclerView: androidx.recyclerview.widget.RecyclerView?): Float {
        if (recyclerView == null) {
            return 0f
        }

        val verticalScrollOffset = recyclerView.computeVerticalScrollOffset()
        val verticalScrollRange = recyclerView.computeVerticalScrollRange()
        val rangeDiff = (verticalScrollRange - viewHeight).toFloat()
        val proportion = verticalScrollOffset.toFloat() / if (rangeDiff > 0) rangeDiff else 1f
        return viewHeight * proportion
    }

    private fun getValueInRange(min: Int, max: Int, value: Int): Int {
        val minimum = Math.max(min, value)
        return Math.min(minimum, max)
    }

    private fun setViewPositions(y: Float) {
        bubbleHeight = bubbleView!!.measuredHeight
        handleHeight = handleView!!.measuredHeight

        val bubbleY = getValueInRange(0, viewHeight - bubbleHeight - handleHeight / 2, (y - bubbleHeight).toInt())
        val handleY = getValueInRange(0, viewHeight - handleHeight, (y - handleHeight / 2).toInt())

        if (showBubble) {
            bubbleView!!.y = bubbleY.toFloat()
        }

        handleView!!.y = handleY.toFloat()
    }

    private fun updateViewHeights() {
        val measureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        bubbleView!!.measure(measureSpec, measureSpec)
        bubbleHeight = bubbleView!!.measuredHeight
        handleView!!.measure(measureSpec, measureSpec)
        handleHeight = handleView!!.measuredHeight
    }

    private fun findFirstVisibleItemPosition(layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager): Int {
        if (layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
            return layoutManager.findFirstVisibleItemPosition()
        } else if (layoutManager is androidx.recyclerview.widget.StaggeredGridLayoutManager) {
            return layoutManager.findFirstVisibleItemPositions(null)[0]
        }

        return 0
    }

    private fun isLayoutReversed(layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager): Boolean {
        if (layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
            return layoutManager.reverseLayout
        } else if (layoutManager is androidx.recyclerview.widget.StaggeredGridLayoutManager) {
            return layoutManager.reverseLayout
        }

        return false
    }

    private fun isViewVisible(view: View?): Boolean {
        return view != null && view.visibility == View.VISIBLE
    }

    private fun cancelAnimation(animator: ViewPropertyAnimator?) {
        animator?.cancel()
    }

    private fun showBubble() {
        if (!isViewVisible(bubbleView)) {
            bubbleView!!.visibility = View.VISIBLE
            bubbleAnimator = bubbleView!!.animate().alpha(1f)
                .setDuration(BUBBLE_ANIM_DURATION.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    // adapter required for new alpha value to stick
                })
        }
    }

    private fun hideBubble() {
        if (isViewVisible(bubbleView)) {
            bubbleAnimator = bubbleView!!.animate().alpha(0f)
                .setDuration(BUBBLE_ANIM_DURATION.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        bubbleView!!.visibility = View.GONE
                        bubbleAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        super.onAnimationCancel(animation)
                        bubbleView!!.visibility = View.GONE
                        bubbleAnimator = null
                    }
                })
        }
    }

    private fun showScrollbar() {
        if (recyclerView!!.computeVerticalScrollRange() - viewHeight > 0) {
            val transX = resources.getDimensionPixelSize(R.dimen.fastscroll_scrollbar_padding_end).toFloat()

           scrollbar!!.translationX = transX
            scrollbar!!.visibility = View.VISIBLE
            scrollbarAnimator = scrollbar!!.animate().translationX(0f).alpha(1f)
                .setDuration(SCROLLBAR_ANIM_DURATION.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    // adapter required for new alpha value to stick
                })
        }
    }

    private fun hideScrollbar() {
       val transX = resources.getDimensionPixelSize(R.dimen.fastscroll_scrollbar_padding_end).toFloat()

        scrollbarAnimator = scrollbar!!.animate().translationX(transX).alpha(0f)
            .setDuration(SCROLLBAR_ANIM_DURATION.toLong())
            .setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    scrollbar!!.visibility = View.GONE
                    scrollbarAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    scrollbar!!.visibility = View.GONE
                    scrollbarAnimator = null
                }
            })
    }

    private fun setHandleSelected(selected: Boolean) {
        handleView!!.isSelected = selected
//        DrawableCompat.setTint(handleImage!!, if (selected) bubbleColor else handleColor)
    }

    private fun layout(context: Context, attrs: AttributeSet?) {
        layout(context, attrs, Size.NORMAL)
    }

    private fun layout(context: Context, size: Size) {
        layout(context, null, size)
    }

    private fun layout(context: Context, attrs: AttributeSet?, size: Size) {
        View.inflate(context, R.layout.fast_scroller, this)

        clipChildren = false
        orientation = LinearLayout.HORIZONTAL

        bubbleView = findViewById(R.id.fastscroll_bubble)
        handleView = findViewById(R.id.fastscroll_handle)
        trackView = findViewById(R.id.fastscroll_track)
        scrollbar = findViewById(R.id.fastscroll_scrollbar)

        bubbleSize = size

        @ColorInt var bubbleColor = Color.GRAY
        @ColorInt var handleColor = Color.DKGRAY
        @ColorInt var trackColor = Color.LTGRAY
        @ColorInt var textColor = Color.WHITE

        var hideScrollbar = true
        var showBubble = true
        var showTrack = false

        var textSize = resources.getDimension(size.textSizeId)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FastScroller, 0, 0)

            if (typedArray != null) {
                try {
                    bubbleColor = typedArray.getColor(R.styleable.FastScroller_bubbleColor, bubbleColor)
                    handleColor = typedArray.getColor(R.styleable.FastScroller_handleColor, handleColor)
                    trackColor = typedArray.getColor(R.styleable.FastScroller_trackColor, trackColor)
                    textColor = typedArray.getColor(R.styleable.FastScroller_bubbleTextColor, textColor)
                    hideScrollbar = typedArray.getBoolean(R.styleable.FastScroller_hideScrollbar, hideScrollbar)
                    showBubble = typedArray.getBoolean(R.styleable.FastScroller_showBubble, showBubble)
                    showTrack = typedArray.getBoolean(R.styleable.FastScroller_showTrack, showTrack)

                    val sizeOrdinal = typedArray.getInt(R.styleable.FastScroller_bubbleSize, size.ordinal)
                    bubbleSize = Size.fromOrdinal(sizeOrdinal)

                    textSize = typedArray.getDimension(
                        R.styleable.FastScroller_bubbleTextSize,
                        resources.getDimension(bubbleSize!!.textSizeId)
                    )
                } finally {
                    typedArray.recycle()
                }
            }
        }

        setTrackColor(trackColor)
        setHandleColor(handleColor)
        setBubbleColor(bubbleColor)
        setBubbleTextColor(textColor)
        setHideScrollbar(hideScrollbar)
        setBubbleVisible(showBubble)
        setTrackVisible(showTrack)

        bubbleView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    /**
     * A FastScrollListener can be added to a [FastScroller] to receive messages when a
     * fast scrolling event has occurred.
     *
     * @see FastScroller.setFastScrollListener
     */
    interface FastScrollListener {

        /**
         * Called when fast scrolling begins.
         */
        fun onFastScrollStart(fastScroller: FastScroller)

        /**
         * Called when fast scrolling ends.
         */
        fun onFastScrollStop(fastScroller: FastScroller)
    }

    /**
     * A SectionIndexer can be added to a [FastScroller] to provide the text to display
     * for the visible section while fast scrolling.
     *
     * @see FastScroller.setSectionIndexer
     */
    interface SectionIndexer {

        /**
         * Get the text to be displayed for the visible section at the current position.
         *
         * @param position The current position of the visible section
         * @return The text to display
         */
        fun getSectionText(position: Int): CharSequence
    }

    companion object {

        private val BUBBLE_ANIM_DURATION = 100
        private val SCROLLBAR_ANIM_DURATION = 300
        private val SCROLLBAR_HIDE_DELAY = 1000
        private val TRACK_SNAP_RANGE = 5
    }
}
