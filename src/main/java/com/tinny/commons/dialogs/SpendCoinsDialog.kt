package com.tinny.commons.dialogs

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R
import com.tinny.commons.animations.AnimationUtils
import com.tinny.commons.databinding.RateUsBinding
import com.tinny.commons.databinding.SpendCoinsBinding
import com.tinny.commons.extentions.setFontWithColor
import com.tinny.commons.helper.AppLogger
import com.tinny.commons.helper.CommonConstants


class SpendCoinsDialog(val activity: AppCompatActivity,
    message: String,
    positiveButtonText: String = activity.getString(R.string.ok),
    negativeButtonText: String = activity.getString(R.string.cancel),
    val callback: (value: Int) -> Unit
    ) : DialogInterface.OnClickListener {

    private var binding: SpendCoinsBinding =
        SpendCoinsBinding.inflate(LayoutInflater.from(activity),null,false)
        //val view: View = LayoutInflater.from(activity.baseContext).inflate(R.layout.spend_coins, null)
        val alertDialog = AlertDialog.Builder(activity).setView(binding.root)
        val alert = alertDialog.setCancelable(false)
        .setPositiveButton(positiveButtonText, this)
        .setNegativeButton(negativeButtonText, this)
        .create()
        init {
            binding.txtMessage.text = message

            binding.img.maxValue = 500f
            val animationLocal = ObjectAnimator.ofInt(binding.img, "progress", 0, 500)
            animationLocal.duration = 10000L
            animationLocal.interpolator = AccelerateInterpolator()
            animationLocal.start()

            binding.txtLife.setFontWithColor(
                context = activity,
                color = activity.resources.getColor(R.color.md_red_500),
                fileName = CommonConstants.icomoonCommon,
                font = activity.getString(R.string.f_fav)
            )

            animationLocal.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {

                    try {
                        if (alert.isShowing) {
                            alert.dismiss()
                            callback(CommonConstants.NoResponse)
                        }
                    } catch (e: Exception) {
                        e.toString()
                    }

                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationStart(p0: Animator) {
                }

            })

            AnimationUtils.pulseAnimation(view = binding.txtLife, repeatCount = 10) {
            }
            if (!activity.isFinishing) {
                alert.setOnShowListener {
                    AppLogger.debugLogs("dialog show =====", "dialog show =====")

                    val btn = alert.getButton(AlertDialog.BUTTON_POSITIVE)
                    btn.setTextColor(activity.resources.getColor(R.color.md_red_500))
                    btn.typeface = Typeface.DEFAULT_BOLD
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(activity.resources.getColor(R.color.md_grey_700));
                }
                alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
                try {
                    alert.show()
                }catch (e:Exception){
                    e.printStackTrace()
                }


            }
        }
        override fun onClick(dialog: DialogInterface?, which: Int) {
            dialog!!.dismiss()
            when(which){
                DialogInterface.BUTTON_POSITIVE->{callback(CommonConstants.Continue)}
                DialogInterface.BUTTON_NEGATIVE->{callback(CommonConstants.TryAgain)}
            }

        }
}