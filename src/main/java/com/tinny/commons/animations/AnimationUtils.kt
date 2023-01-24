package com.tinny.commons.animations

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object AnimationUtils {

    fun fadeInWithTransition(view: View, duration: Long = 1000, callback: () -> Unit) {
        val anim = getAnimator(duration)
        anim.playTogether(
            ObjectAnimator.ofFloat(view, "translationX", 1f, 0f),
            ObjectAnimator.ofFloat(view, "translationY", 1f, 0f),
            ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        )
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                callback()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
            }

        })
    }




    fun fadeOutWithTransition(view: View, duration: Long = 1000, callback: () -> Unit) {
        val anim = getAnimator(duration)
        anim.playTogether(
            ObjectAnimator.ofFloat(view, "translationX", 0f, 1f),
            ObjectAnimator.ofFloat(view, "translationY", 0f, 1f),
            ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        )
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                callback()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
            }

        })
    }


    fun shakeAnimation(view: View, animationTime: Long = 1000, callback: () -> Unit) {
        val anim = getAnimator(animationTime)
        anim.playTogether(
            ObjectAnimator.ofFloat(view, "rotation", 0f, 10f, -10f, 20f, -20f, 10f, -10f, 6f, -6f, 3f, -3f, 0f),
            ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f),
            ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f)
        )
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                callback()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
            }

        })
    }

    fun shakeAnimationForLife(view: View, animationTime: Long = 1000, callback: () -> Unit) {
        val anim = AnimatorSet()
        anim.duration = animationTime
        anim.playTogether(
            ObjectAnimator.ofFloat(view, "rotation", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f),
            ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f),
            ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f)
        )
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                callback()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
            }

        })
    }


    fun getAnimator(duration: Long = 1000): AnimatorSet {
        val animatorSet = AnimatorSet()
        animatorSet.duration = duration
        return animatorSet
    }

    fun pulseAnimation(view: View, repeatCount: Int = 10, animationTime: Long = 1000, callback: () -> Unit) {
        val anim = AnimatorSet()
        var maxRep = repeatCount
        anim.duration = animationTime
        anim.playTogether(
            ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f),
            ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f)
        )

        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                maxRep--
                if (maxRep!=0){
                    anim.start()
                }else {
                    callback()
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationStart(p0: Animator) {
            }

        })
    }
}
