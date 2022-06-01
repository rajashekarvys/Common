package com.tinny.commons.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView

object ViewAnim {

    private val DECCELERATE_INTERPOLATOR = DecelerateInterpolator()
    private val ACCELERATE_DECELERATE_INTERPOLATOR = AccelerateDecelerateInterpolator()
    private val OVERSHOOT_INTERPOLATOR = OvershootInterpolator(4f)

    private var animatorSet: AnimatorSet? = null
    fun expodeAnim(txtAnim: TextView, dots: DotsView) {
        if (animatorSet != null) {
            animatorSet!!.cancel()
        }

        txtAnim.animate().cancel()
        txtAnim.scaleX = 0f
        txtAnim.scaleY = 0f

        dots.currentProgress = 0f

        animatorSet = AnimatorSet()

        /*   ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(vCircle, CircleView.OUTER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
        outerCircleAnimator.setDuration(250);
        outerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(vCircle, CircleView.INNER_CIRCLE_RADIUS_PROGRESS, 0.1f, 1f);
        innerCircleAnimator.setDuration(200);
        innerCircleAnimator.setStartDelay(200);
        innerCircleAnimator.setInterpolator(DECCELERATE_INTERPOLATOR);*/

        val starScaleYAnimator = ObjectAnimator.ofFloat(txtAnim, ImageView.SCALE_Y, 0.5f, 1f)
        starScaleYAnimator.duration = 350
        starScaleYAnimator.startDelay = 250
        starScaleYAnimator.interpolator = OVERSHOOT_INTERPOLATOR

        val starScaleXAnimator = ObjectAnimator.ofFloat(txtAnim, ImageView.SCALE_X, 0.5f, 1f)
        starScaleXAnimator.duration = 350
        starScaleXAnimator.startDelay = 250
        starScaleXAnimator.interpolator = OVERSHOOT_INTERPOLATOR

        val dotsAnimator = ObjectAnimator.ofFloat(dots, dots.DOTS_PROGRESS, 0f, 1f)
        dotsAnimator.duration = 900
        dotsAnimator.startDelay = 50
        dotsAnimator.interpolator = ACCELERATE_DECELERATE_INTERPOLATOR

        animatorSet!!.playTogether(
            /* outerCircleAnimator,
                innerCircleAnimator,*/
            starScaleYAnimator,
            starScaleXAnimator,
            dotsAnimator
        )

        animatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator) {
                dots.currentProgress = 0f
                txtAnim.scaleX = 1f
                txtAnim.scaleY = 1f
            }
        })

        animatorSet!!.start()
    }

}