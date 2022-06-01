package com.tinny.commons.extentions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.CycleInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import com.tinny.commons.helper.SafeClickListener


fun View.isVisibile(): Boolean = visibility == View.VISIBLE

fun View.isGone(): Boolean = visibility == View.GONE

fun View.isInvisible(): Boolean = visibility == View.INVISIBLE


fun View.makeVisible() {
    visibility = View.VISIBLE
    alpha = 1f
}

fun View.makeGone() {
    visibility = View.GONE
    alpha = 0f
}

fun View.makeVisibleAnim(duration: Long = 300) {
    animate()
        .alpha(1f)
        .scaleX(1f)
        .scaleY(1f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = View.VISIBLE
            }
        })
}

fun View.makeGoneAnim(duration: Long = 300,callback: () -> Unit) {
    animate()
        .alpha(0f)
        .scaleX(0.2f)
        .scaleY(0.2f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
                callback()
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

fun View.setSafeOnClickListener(defaultInterval: Int = 1000,onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(defaultInterval) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.setSafeOnClickListenerWithScale(defaultInterval: Int = 1000,onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(defaultInterval) {
        val animatorCompat = ViewCompat.animate(it)
            .setDuration(200L)
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setInterpolator(CycleInterpolator(0.5f))
        animatorCompat.start()
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}


fun View.elasticAnim(){
    val animatorCompat = ViewCompat.animate(this)
        .setDuration(200L)
        .scaleX(0.8f)
        .scaleY(0.8f)
        .setInterpolator(CycleInterpolator(0.5f))
        .setListener( object :ViewPropertyAnimatorListener {
            override fun onAnimationEnd(view: View?) {
                view!!.scaleX =1f
                view!!.scaleY =1f

            }

            override fun onAnimationCancel(view: View?) {
            }

            override fun onAnimationStart(view: View?) {
            }

        })
    animatorCompat.start()


}
fun View.changeDrawableColor(drawable: Drawable,color:Int){
    val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable)
    DrawableCompat.setTint(wrappedDrawable, color)
}