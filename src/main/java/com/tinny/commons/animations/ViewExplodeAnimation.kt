package com.tinny.commons.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.tinny.commons.R
import com.tinny.commons.databinding.ActivityAboutBinding
import com.tinny.commons.databinding.ViewExpoldeBinding
import com.tinny.commons.extentions.makeVisible

class ViewExplodeAnimation : FrameLayout {
    private val DECCELERATE_INTERPOLATOR = DecelerateInterpolator()
    private val ACCELERATE_DECELERATE_INTERPOLATOR = AccelerateDecelerateInterpolator()
    private val OVERSHOOT_INTERPOLATOR = OvershootInterpolator(4f)
    private var animatorSet: AnimatorSet? = null
    private lateinit var binding: ViewExpoldeBinding


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }

    private fun init() {
        binding = ViewExpoldeBinding.inflate(LayoutInflater.from(context),this)
        val view = binding.root
    }

    fun setText(text: String) {
        binding.txtAnim.text = text
    }

    fun setTextViewBackground(mDrawable: Drawable) {
        binding.txtAnim.setBackgroundDrawable(mDrawable)
    }

    fun getText(): String {
        return binding.txtAnim.text.toString()
    }

    fun doClick() {
        binding.dots.makeVisible()
        if (animatorSet != null) {
            animatorSet!!.cancel()
        }

        binding.txtAnim.animate().cancel()
        binding.txtAnim.scaleX = 0f
        binding.txtAnim.scaleY = 0f

        binding.dots.currentProgress = 0f

        animatorSet = AnimatorSet()

        /*   ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(vCircle, CircleView.OUTER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
        outerCircleAnimator.setDuration(250);
        outerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(vCircle, CircleView.INNER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
        innerCircleAnimator.setDuration(200);
        innerCircleAnimator.setStartDelay(200);
        innerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);*/

        val starScaleYAnimator = ObjectAnimator.ofFloat(binding.txtAnim, ImageView.SCALE_Y, 0.5f, 1f)
        starScaleYAnimator.duration = 1000
        starScaleYAnimator.startDelay = 350
        starScaleYAnimator.interpolator = OVERSHOOT_INTERPOLATOR
        starScaleYAnimator.repeatCount = 2


        val starScaleXAnimator = ObjectAnimator.ofFloat(binding.txtAnim, ImageView.SCALE_X, 0.5f, 1f)
        starScaleXAnimator.duration = 1000
        starScaleXAnimator.startDelay = 350
        starScaleXAnimator.interpolator = OVERSHOOT_INTERPOLATOR
        starScaleYAnimator.repeatCount = 2

        val dotsAnimator = ObjectAnimator.ofFloat(binding.dots, binding.dots.DOTS_PROGRESS, 0f, 1f)
        dotsAnimator.duration = 1900
        dotsAnimator.startDelay = 50
        dotsAnimator.interpolator = ACCELERATE_DECELERATE_INTERPOLATOR
        starScaleYAnimator.repeatCount = 2

        animatorSet!!.playTogether(
            /* outerCircleAnimator,
                innerCircleAnimator,*/
            starScaleYAnimator,
            starScaleXAnimator,
            dotsAnimator
        )

        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator) {
                binding.dots.currentProgress = 0f
                binding.txtAnim.scaleX = 1f
                binding.txtAnim.scaleY = 1f
            }
        })
        animatorSet!!.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                binding.txtAnim.animate().scaleX(0.7f).scaleY(0.7f).setDuration(150).interpolator =
                    DECCELERATE_INTERPOLATOR
                isPressed = true
            }

            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                val isInside = x > 0 && x < width && y > 0 && y < height
                if (isPressed != isInside) {
                    isPressed = isInside
                }
            }

            MotionEvent.ACTION_UP -> {
                binding.txtAnim.animate().scaleX(1f).scaleY(1f).interpolator = DECCELERATE_INTERPOLATOR
                if (isPressed) {
                    doClick()
                    isPressed = false
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                binding.txtAnim.animate().scaleX(1f).scaleY(1f).interpolator = DECCELERATE_INTERPOLATOR
                isPressed = false
            }
        }
        return true
    }


}
