package com.tinny.commons.dialogs
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R
import com.tinny.commons.databinding.AtingDialogBinding
import com.tinny.commons.databinding.RateUsBinding
import com.tinny.commons.extentions.launchViewIntent
import com.tinny.commons.extentions.setFontWithColor
import com.tinny.commons.extentions.toast
import com.tinny.commons.helper.CommonConstants

class RateUsDialog(
    val activity: AppCompatActivity,
    val message: String,
    val color: Int,
    val icon:Int = 0,
    val callback: () -> Unit
) {
    private var binding: RateUsBinding =
        RateUsBinding.inflate(LayoutInflater.from(activity),null,false)

    lateinit var alertDialog: AlertDialog

    init {
//        view.img.setImageResource(icon)
//        view.header.setBackgroundColor(color)
        binding.txtMessage.text = message
        binding.txtTitle.setTextColor(color)
        binding.imgCancel.setOnClickListener {
            this.alertDialog.dismiss()
        }
        binding.rateus.setOnClickListener {
            activity.launchViewIntent("market://details?id=${activity.packageName}")
            alertDialog.dismiss()
            callback()
        }
        binding.feedback.setOnClickListener {
            this.alertDialog.dismiss()
            activity.toast(activity.getString(R.string.thanks_for_feedback))
            callback()
        }
        binding.imgCancel.setFontWithColor(
            context = activity,
            color = activity.resources.getColor(R.color.md_grey_black),
            fileName = CommonConstants.icomoonCommon,
            font = activity.getString(R.string.f_clear)
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root)
        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    }

}