package com.tinny.commons.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.tinny.commons.R
import com.tinny.commons.extentions.isVisibile
import com.tinny.commons.extentions.makeVisible
import kotlinx.android.synthetic.main.fast_scroll.view.*

class FastScrollRecylerView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var scrollbarAnimator: ViewPropertyAnimator? = null
    private var bubbleAnimator: ViewPropertyAnimator? = null
    private var sectionIndexer: SectionIndexer? = null
    private val BUBBLE_ANIM_DURATION = 100
    private val SCROLLBAR_ANIM_DURATION = 300
    private val SCROLLBAR_HIDE_DELAY = 1000
    private val TRACK_SNAP_RANGE = 5
    lateinit var parentRecyclerView: androidx.recyclerview.widget.RecyclerView
    lateinit var swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    private var bubbleImage: Drawable? = null
    private var handleImage: Drawable? = null
    private var trackImage: Drawable? = null
    private var hideScrollbar: Boolean = false
    private var showBubble: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.fast_scroll, this)
        setHandleColor(Color.GRAY)
    }

    private var viewHeight: Int = 0

    private val scrollbarHider = Runnable { hideScrollbar() }


    fun setRecylerview(recyclerView: androidx.recyclerview.widget.RecyclerView, swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout) {
        this.parentRecyclerView = recyclerView
        parentRecyclerView.setOnScrollListener(scrollListener)
    }

    fun setRecylerview(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        this.parentRecyclerView = recyclerView

    }

    fun setSectionIndexer(@Nullable sectionIndexer: SectionIndexer) {
        this.sectionIndexer = sectionIndexer
    }

    fun setHandleColor(@ColorInt color: Int) {

        if (handleImage == null) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.fastscroll_handle)

            if (drawable != null) {
                handleImage = DrawableCompat.wrap(drawable)
                handleImage!!.mutate()
            }
        }

        DrawableCompat.setTint(handleImage!!, color)
        imgHandle.setImageDrawable(handleImage)
    }

    private fun setPosition(y: Float) {
        val position = y / viewHeight
        val bubbleHeight = txtBubble.height
        txtBubble.y =
            getValueInRange(0, viewHeight - bubbleHeight, ((viewHeight - bubbleHeight) * position).toInt()).toFloat()
        val handleHeight = imgHandle.height
        imgHandle.y =
            getValueInRange(0, viewHeight - handleHeight, ((viewHeight - handleHeight) * position).toInt()).toFloat()
    }

    private fun getValueInRange(min: Int, max: Int, value: Int): Int {
        val minimum = Math.max(min, value)
        return Math.min(minimum, max)
    }

    private fun setRecyclerViewPosition(y: Float) {
        if (parentRecyclerView != null) {
            val itemCount = parentRecyclerView.getAdapter()!!.getItemCount()
            val proportion: Float
            if (txtBubble.getY() == 0f) {
                proportion = 0f
            } else if (txtBubble.getY() + txtBubble.getHeight() >= viewHeight - TRACK_SNAP_RANGE) {
                proportion = 1f
            } else {
                proportion = y / viewHeight.toFloat()
            }
            val targetPos = getValueInRange(0, itemCount - 1, (proportion * itemCount.toFloat()).toInt())
            parentRecyclerView.scrollToPosition(targetPos)
            txtBubble.setText(sectionIndexer!!.getSectionText(targetPos))

        }
    }

    private val scrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

        override fun onScrolled(@NonNull recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            if (!imgHandle.isSelected() && isEnabled) {
                setPosition(getScrollProportion(recyclerView))
            }

            if (swipeRefreshLayout != null && recyclerView.layoutManager != null) {
                val firstVisibleItem = findFirstVisibleItemPosition(parentRecyclerView.layoutManager!!)
                val topPosition = if (recyclerView.childCount === 0) 0 else recyclerView.getChildAt(0).top
                swipeRefreshLayout.isEnabled = firstVisibleItem == 0 && topPosition >= 0
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

        private fun findFirstVisibleItemPosition(@NonNull layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager): Int {
            return when (layoutManager) {
                is androidx.recyclerview.widget.LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                is androidx.recyclerview.widget.StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null)[0]
                is androidx.recyclerview.widget.GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                else -> 0
            }

        }

        override fun onScrollStateChanged(@NonNull recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (isEnabled) {
                when (newState) {
                    androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING -> {
                        handler.removeCallbacks(scrollbarHider)
                        cancelAnimation(scrollbarAnimator)

                        if (!scrollbar.isVisibile()) {
                            showScrollbar()
                        }
                    }

                    androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE -> if (hideScrollbar && !imgHandle.isSelected()) {
                        handler.postDelayed(scrollbarHider, SCROLLBAR_HIDE_DELAY.toLong())
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(@NonNull event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < imgHandle.getX() - ViewCompat.getPaddingStart(scrollbar)) {
                    return false
                }

                requestDisallowInterceptTouchEvent(true)
                setHandleSelected(true)

                handler.removeCallbacks(scrollbarHider)
                cancelAnimation(scrollbarAnimator)
                cancelAnimation(bubbleAnimator)

                if (!scrollbar.isVisibile()) {
                    showScrollbar()
                }

                if (showBubble && sectionIndexer != null) {
                    showBubble()
                }

                /*if (fastScrollListener != null) {
                    fastScrollListener.onFastScrollStart(this)
                }*/
                val y = event.y
                setPosition(y)
                setRecyclerViewPosition(y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                setPosition(y)
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


                return true
            }
        }

        return super.onTouchEvent(event)
    }


    interface SectionIndexer {

        /**
         * Get the text to be displayed for the visible section at the current position.
         *
         * @param position The current position of the visible section
         * @return The text to display
         */
        fun getSectionText(position: Int): CharSequence
    }

    private fun showBubble() {
        if (!txtBubble.isVisibile()) {
            txtBubble.makeVisible()
            bubbleAnimator = txtBubble.animate().alpha(1f)
                .setDuration(BUBBLE_ANIM_DURATION.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    // adapter required for new alpha value to stick
                })
        }
    }

    private fun hideBubble() {
        if (txtBubble.isVisibile()) {
            bubbleAnimator = txtBubble.animate().alpha(0f)
                .setDuration(BUBBLE_ANIM_DURATION.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        txtBubble.setVisibility(View.GONE)
                        bubbleAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        super.onAnimationCancel(animation)
                        txtBubble.setVisibility(View.GONE)
                        bubbleAnimator = null
                    }
                })
        }
    }

    private fun showScrollbar() {
        if (parentRecyclerView.computeVerticalScrollRange() - viewHeight > 0) {
            val transX = 16f
            scrollbar.setTranslationX(transX)
            scrollbar.setVisibility(View.VISIBLE)
            scrollbarAnimator = scrollbar.animate().translationX(0f).alpha(1f)
                .setDuration(SCROLLBAR_ANIM_DURATION.toLong())
                .setListener(object : AnimatorListenerAdapter() {

                    // adapter required for new alpha value to stick
                })
        }
    }

    private fun hideScrollbar() {
        val transX = 16f

        scrollbarAnimator = scrollbar.animate().translationX(transX).alpha(0f)
            .setDuration(SCROLLBAR_ANIM_DURATION.toLong())
            .setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    scrollbar.setVisibility(View.GONE)
                    scrollbarAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    scrollbar.setVisibility(View.GONE)
                    scrollbarAnimator = null
                }
            })
    }

    private fun setHandleSelected(selected: Boolean) {
        imgHandle.isSelected = selected
        DrawableCompat.setTint(handleImage!!, Color.GREEN)
    }

    private fun cancelAnimation(animator: ViewPropertyAnimator?) {
        animator?.cancel()
    }
}