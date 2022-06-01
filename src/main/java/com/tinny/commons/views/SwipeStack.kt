package com.tinny.commons.views

import android.content.Context
import android.database.DataSetObserver
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.FrameLayout
import com.tinny.commons.R
import com.tinny.commons.helper.SwipeHelper
import java.util.*

/**
 * This view is used to place the view in stack manner user can see one card on top of another.
 * Can easily swipe card left or right
 * */

class SwipeStack @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {

    /**
     * Returns the adapter currently in use in this SwipeStack.
     *
     * @return The adapter currently used to display data in this SwipeStack.
     */
    /**
     * Sets the data behind this SwipeView.
     *
     * @param adapter The Adapter which is responsible for maintaining the
     * data backing this list and for producing a view to represent an
     * item in that data set.
     * @see .getAdapter
     */
    var adapter: Adapter? = null
        set(adapter) {
            if (this.adapter != null) this.adapter!!.unregisterDataSetObserver(mDataObserver)
            field = adapter
            this.adapter!!.registerDataSetObserver(mDataObserver)
        }
    private var mRandom: Random? = null

    /**
     * Returns the allowed swipe directions.
     *
     * @return The currently allowed swipe directions.
     */
    /**
     * Sets the allowed swipe directions.
     *
     * @param directions One of [.SWIPE_DIRECTION_BOTH],
     * [.SWIPE_DIRECTION_ONLY_LEFT], or [.SWIPE_DIRECTION_ONLY_RIGHT].
     */
    var allowedSwipeDirections: Int = 0
    private var mAnimationDuration: Int = 0
    private var mCurrentViewIndex: Int = 0
    private var mNumberOfStackedViews: Int = 0
    private var mViewSpacing: Int = 0
    private var mViewRotation: Int = 0
    private var mSwipeRotation: Float = 0.toFloat()
    private var mSwipeOpacity: Float = 0.toFloat()
    private var mScaleFactor: Float = 0.toFloat()
    private var mDisableHwAcceleration: Boolean = false
    private var mIsFirstLayout = true

    /**
     * Get the view from the top of the stack.
     *
     * @return The view if the stack is not empty or null otherwise.
     */
    var topView: View? = null
        private set
    private var mSwipeHelper: SwipeHelper? = null
    private var mDataObserver: DataSetObserver? = null
    private var mListener: SwipeStackListener? = null
    private var mProgressListener: SwipeProgressListener? = null

    /**
     * Returns the current adapter position.
     *
     * @return The current position.
     */
    val currentPosition: Int
        get() = mCurrentViewIndex - childCount

    init {
        readAttributes(attrs)
        initialize()
    }

    private fun readAttributes(attributeSet: AttributeSet?) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.SwipeStack)

        try {
            allowedSwipeDirections = attrs.getInt(
                R.styleable.SwipeStack_allowed_swipe_directions,
                SWIPE_DIRECTION_BOTH
            )
            mAnimationDuration = attrs.getInt(
                R.styleable.SwipeStack_animation_duration,
                DEFAULT_ANIMATION_DURATION
            )
            mNumberOfStackedViews = attrs.getInt(R.styleable.SwipeStack_stack_size, DEFAULT_STACK_SIZE)
            mViewSpacing = attrs.getDimensionPixelSize(
                R.styleable.SwipeStack_stack_spacing,
                resources.getDimensionPixelSize(R.dimen.default_stack_spacing)
            )
            mViewRotation = attrs.getInt(R.styleable.SwipeStack_stack_rotation, DEFAULT_STACK_ROTATION)
            mSwipeRotation = attrs.getFloat(R.styleable.SwipeStack_swipe_rotation, DEFAULT_SWIPE_ROTATION)
            mSwipeOpacity = attrs.getFloat(R.styleable.SwipeStack_swipe_opacity, DEFAULT_SWIPE_OPACITY)
            mScaleFactor = attrs.getFloat(R.styleable.SwipeStack_scale_factor, DEFAULT_SCALE_FACTOR)
            mDisableHwAcceleration = attrs.getBoolean(
                R.styleable.SwipeStack_disable_hw_acceleration,
                DEFAULT_DISABLE_HW_ACCELERATION
            )
        } finally {
            attrs.recycle()
        }
    }

    private fun initialize() {
        mRandom = Random()

        clipToPadding = false
        clipChildren = false

        mSwipeHelper = SwipeHelper(this)
        mSwipeHelper!!.setAnimationDuration(mAnimationDuration)
        mSwipeHelper!!.setRotation(mSwipeRotation)
        mSwipeHelper!!.setOpacityEnd(mSwipeOpacity)

        mDataObserver = object : DataSetObserver() {
            override fun onChanged() {
                super.onChanged()
                invalidate()
                requestLayout()
            }
        }
    }

    fun disableTouch(touch:Boolean) {
        mSwipeHelper!!.disableTouch(touch)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        bundle.putInt(KEY_CURRENT_INDEX, mCurrentViewIndex - childCount)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) {
            val bundle = state as Bundle?
            mCurrentViewIndex = bundle!!.getInt(KEY_CURRENT_INDEX)
            state = bundle.getParcelable(KEY_SUPER_STATE)
        }

        super.onRestoreInstanceState(state)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        if (adapter == null || adapter!!.isEmpty) {
            mCurrentViewIndex = 0
            removeAllViewsInLayout()
            return
        }

        var x = childCount
        while (x < mNumberOfStackedViews && mCurrentViewIndex < adapter!!.count) {
            addNextView()
            x++
        }

        reorderItems()

        mIsFirstLayout = false
    }

    private fun addNextView() {
        if (mCurrentViewIndex < adapter!!.count) {
            val bottomView = adapter!!.getView(mCurrentViewIndex, null, this)
            bottomView.setTag(R.id.new_view, true)

            if (!mDisableHwAcceleration) {
                bottomView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            }

            if (mViewRotation > 0) {
                bottomView.rotation = (mRandom!!.nextInt(mViewRotation) - mViewRotation / 2).toFloat()
            }

            val width = width - (paddingLeft + paddingRight)
            val height = height - (paddingTop + paddingBottom)

            var params: ViewGroup.LayoutParams? = bottomView.layoutParams
            if (params == null) {
                params = ViewGroup.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            }

            var measureSpecWidth = View.MeasureSpec.AT_MOST
            var measureSpecHeight = View.MeasureSpec.AT_MOST

            if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                measureSpecWidth = View.MeasureSpec.EXACTLY
            }

            if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                measureSpecHeight = View.MeasureSpec.EXACTLY
            }

            bottomView.measure(measureSpecWidth or width, measureSpecHeight or height)
            addViewInLayout(bottomView, 0, params, true)

            mCurrentViewIndex++
        }
    }

    private fun reorderItems() {
        for (x in 0 until childCount) {
            val childView = getChildAt(x)
            val topViewIndex = childCount - 1

            val distanceToViewAbove = topViewIndex * mViewSpacing - x * mViewSpacing
            val newPositionX = (width - childView.measuredWidth) / 2
            val newPositionY = distanceToViewAbove + paddingTop

            childView.layout(
                newPositionX,
                paddingTop,
                newPositionX + childView.measuredWidth,
                paddingTop + childView.measuredHeight
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                childView.translationZ = x.toFloat()
            }

            val isNewView = childView.getTag(R.id.new_view) as Boolean
            val scaleFactor = Math.pow(mScaleFactor.toDouble(), (childCount - x).toDouble()).toFloat()

            if (x == topViewIndex) {
                mSwipeHelper!!.unregisterObservedView()
                topView = childView
                mSwipeHelper!!.registerObservedView(topView, newPositionX.toFloat(), newPositionY.toFloat())
            }

            if (!mIsFirstLayout) {

                if (isNewView) {
                    childView.setTag(R.id.new_view, false)
                    childView.alpha = 0f
                    childView.y = newPositionY.toFloat()
                    childView.scaleY = scaleFactor
                    childView.scaleX = scaleFactor
                }

                childView.animate()
                    .y(newPositionY.toFloat())
                    .scaleX(scaleFactor)
                    .scaleY(scaleFactor)
                    .alpha(1f).duration = mAnimationDuration.toLong()

            } else {
                childView.setTag(R.id.new_view, false)
                childView.y = newPositionY.toFloat()
                childView.scaleY = scaleFactor
                childView.scaleX = scaleFactor
            }
        }
    }

    private fun removeTopView() {
        if (topView != null) {
            removeView(topView)
            topView = null
        }

        if (childCount == 0) {
            if (mListener != null) mListener!!.onStackEmpty()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    fun onSwipeStart() {
        if (mProgressListener != null) mProgressListener!!.onSwipeStart(currentPosition)
    }

    fun onSwipeProgress(progress: Float) {
        if (mProgressListener != null)
            mProgressListener!!.onSwipeProgress(currentPosition, progress)
    }

    fun onSwipeEnd() {
        if (mProgressListener != null) mProgressListener!!.onSwipeEnd(currentPosition)
    }

    fun onViewSwipedToLeft() {
        if (mListener != null) mListener!!.onViewSwipedToLeft(currentPosition)
        removeTopView()
    }

    fun onViewSwipedToRight() {
        if (mListener != null) mListener!!.onViewSwipedToRight(currentPosition)
        removeTopView()
    }

    /**
     * Register a callback to be invoked when the user has swiped the top view
     * left / right or when the stack gets empty.
     *
     * @param listener The callback that will run
     */
    fun setListener(listener: SwipeStackListener?) {
        mListener = listener
    }

    /**
     * Register a callback to be invoked when the user starts / stops interacting
     * with the top view of the stack.
     *
     * @param listener The callback that will run
     */
    fun setSwipeProgressListener(listener: SwipeProgressListener?) {
        mProgressListener = listener
    }

    /**
     * Programmatically dismiss the top view to the right.
     */
    fun swipeTopViewToRight() {
        if (childCount == 0) return
        mSwipeHelper!!.swipeViewToRight()
    }

    /**
     * Programmatically dismiss the top view to the left.
     */
    fun swipeTopViewToLeft() {
        if (childCount == 0) return
        mSwipeHelper!!.swipeViewToLeft()
    }

    /**
     * Resets the current adapter position and repopulates the stack.
     */
    fun resetStack() {
        mCurrentViewIndex = 0
        removeAllViewsInLayout()
        requestLayout()
    }

    /**
     * Interface definition for a callback to be invoked when the top view was
     * swiped to the left / right or when the stack gets empty.
     */
    interface SwipeStackListener {
        /**
         * Called when a view has been dismissed to the left.
         *
         * @param position The position of the view in the adapter currently in use.
         */
        fun onViewSwipedToLeft(position: Int)

        /**
         * Called when a view has been dismissed to the right.
         *
         * @param position The position of the view in the adapter currently in use.
         */
        fun onViewSwipedToRight(position: Int)

        /**
         * Called when the last view has been dismissed.
         */
        fun onStackEmpty()
    }

    /**
     * Interface definition for a callback to be invoked when the user
     * starts / stops interacting with the top view of the stack.
     */
    interface SwipeProgressListener {
        /**
         * Called when the user starts interacting with the top view of the stack.
         *
         * @param position The position of the view in the currently set adapter.
         */
        fun onSwipeStart(position: Int)

        /**
         * Called when the user is dragging the top view of the stack.
         *
         * @param position The position of the view in the currently set adapter.
         * @param progress Represents the horizontal dragging position in relation to
         * the start position of the drag.
         */
        fun onSwipeProgress(position: Int, progress: Float)

        /**
         * Called when the user has stopped interacting with the top view of the stack.
         *
         * @param position The position of the view in the currently set adapter.
         */
        fun onSwipeEnd(position: Int)
    }

    companion object {

        val SWIPE_DIRECTION_BOTH = 0
        val SWIPE_DIRECTION_ONLY_LEFT = 1
        val SWIPE_DIRECTION_ONLY_RIGHT = 2

        val DEFAULT_ANIMATION_DURATION = 300
        val DEFAULT_STACK_SIZE = 3
        val DEFAULT_STACK_ROTATION = 8
        val DEFAULT_SWIPE_ROTATION = 30f
        val DEFAULT_SWIPE_OPACITY = 1f
        val DEFAULT_SCALE_FACTOR = 1f
        val DEFAULT_DISABLE_HW_ACCELERATION = true

        private val KEY_SUPER_STATE = "superState"
        private val KEY_CURRENT_INDEX = "currentIndex"
    }
}
