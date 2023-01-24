package com.tinny.commons.dialogs

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R
import com.tinny.commons.animations.AnimationUtils
import com.tinny.commons.databinding.UpgradeToProBinding
import com.tinny.commons.databinding.WatchVideoBinding
import com.tinny.commons.extentions.changeDrawableColor
import com.tinny.commons.extentions.setFontWithColor
import com.tinny.commons.helper.CommonConstants
import com.tinny.commons.helper.CommonConstants.NoResponse
import com.tinny.commons.helper.CommonConstants.TryAgain
import com.tinny.commons.helper.CommonConstants.WatchVideo

class WatchVideoDialog
    (
    val activity: AppCompatActivity,
    val message: String = activity.getString(R.string.watch_video),
    val title: String = "",
    val callback: (Int) -> Unit
) {
    val view: View = LayoutInflater.from(activity).inflate(R.layout.watch_video, null)
    private var binding: WatchVideoBinding =
        WatchVideoBinding.inflate(LayoutInflater.from(activity),null,false)
    lateinit var alertDialog: AlertDialog

    init {
        binding.imgCancel.setOnClickListener {
            this.alertDialog.dismiss()
            callback(NoResponse)
        }
        binding.btnWatchVideo.setOnClickListener {
            alertDialog.dismiss()
            callback(WatchVideo)
        }
        binding.btnTryAgain.setOnClickListener {
            this.alertDialog.dismiss()
            callback(TryAgain)
        }


        binding.imgCancel.setFontWithColor(
            context = activity,
            color = activity.resources.getColor(R.color.md_grey_black),
            fileName = CommonConstants.icomoonCommon,
            font = activity.getString(R.string.f_clear)
        )

        binding.btnWatchVideo.setFontWithColor(
            context = activity,
            color = activity.resources.getColor(R.color.md_grey_white),
            fileName = CommonConstants.icomoonCommon,
            font = activity.getString(R.string.f_video)
        )

        binding.btnTryAgain.changeDrawableColor(
            binding.btnTryAgain.background,
            activity.resources.getColor(R.color.md_grey_500)
        )
        binding.btnTryAgain.setFontWithColor(
            context = activity,
            color = activity.resources.getColor(R.color.md_grey_white),
            fileName = CommonConstants.icomoonCommon,
            font = activity.getString(R.string.f_refresh)
        )
        binding.txtLife.setFontWithColor(
            context = activity,
            color = activity.resources.getColor(R.color.md_red_500),
            fileName = CommonConstants.icomoonCommon,
            font = activity.getString(R.string.f_fav)
        )

        binding.img.maxValue = 500f
        val animationLocal = ObjectAnimator.ofInt(binding.img, "progress", 0, 500)
        animationLocal.duration = 10000L
        animationLocal.interpolator = AccelerateInterpolator()
        animationLocal.start()

        animationLocal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {

                try {
                    if (alertDialog.isShowing) {
                        alertDialog.dismiss()
                        callback(NoResponse)
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
        val dialog = AlertDialog.Builder(activity).setView(view)
        alertDialog = dialog.create()
        alertDialog.setCancelable(false)
        if (!activity.isFinishing) {
            alertDialog.show()
        }
    }

}