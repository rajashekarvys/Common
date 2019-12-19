package com.tinny.commons.extentions

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.R
import android.transition.Fade
import android.transition.Transition
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.opengl.ETC1.getHeight
import androidx.core.view.ViewCompat.animate
import android.R.attr.translationY
import com.tinny.commons.helper.SafeClickListener


fun View.isVisibile(): Boolean = visibility == View.VISIBLE

fun View.isGone(): Boolean = visibility == View.GONE

fun View.isInvisible(): Boolean = visibility == View.INVISIBLE


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeVisibleAnim() {
    animate()
        .alpha(1f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = View.VISIBLE
            }
        })
}

fun View.makeGoneAnim() {
    animate()
        .alpha(0f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
            }
        })
}

/*private fun toggle() {


    TransitionManager.beginDelayedTransition(parent, transition)
    image.setVi sibility(if (show) View.VISIBLE else View.GONE)
}*/
fun View.makeInvisible() {
    visibility = View.INVISIBLE
}


/**
 * Hides the soft input keyboard from the screen
 */
fun View.hideKeyboard(context: Context?) {
    val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.onGlobalLayout(callback: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            callback()
        }
    })
}

fun View.rotateRight() {
    val rotate = ObjectAnimator.ofFloat(this, "rotation", 0f, 90f)
    rotate.duration = 300
    rotate.start()

}

fun View.rotateLeft() {
    val rotate = ObjectAnimator.ofFloat(this, "rotation", 0f, 270f)
    rotate.duration = 200
    rotate.start()

}

fun View.rotateRevers() {
    val rotate = ObjectAnimator.ofFloat(this, "rotation", 0f, 180f)
    rotate.duration = 200
    rotate.start()
}

fun View.rotateViewWithAngle(from: Float, to: Float) {
    val rotate = ObjectAnimator.ofFloat(this, "rotation", from, to)
    rotate.duration = 200
    rotate.start()
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}