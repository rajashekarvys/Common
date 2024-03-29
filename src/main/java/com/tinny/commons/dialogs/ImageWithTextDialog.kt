package com.tinny.commons.dialogs

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Typeface
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.tinny.commons.R
import com.tinny.commons.databinding.AtingDialogBinding
import com.tinny.commons.databinding.ImageWitTextBinding
import com.tinny.commons.helper.AppLogger

class ImageWithTextDialog(
    val activity: Activity,
    message: String,
    title: String = "",
    image: Int,
    private val positiveButtonText: String = activity.getString(R.string.ok),
    negativeButtonText: String = activity.getString(R.string.cancel),
    val callback: (Int,String) -> Unit
    ) : DialogInterface.OnClickListener {
//        val view: View = LayoutInflater.from(activity.baseContext).inflate(R.layout.image_wit_text, null)
    private var binding: ImageWitTextBinding =
        ImageWitTextBinding.inflate(LayoutInflater.from(activity),null,false)

        init {
            binding.txtMessage.text = message
            binding.ratingImg.setImageResource(image)
            if (!activity.isFinishing) {
                val view = binding.root
                val dialog = AlertDialog.Builder(activity).setView(view)
                val alert = dialog.setCancelable(false)
                    .setPositiveButton(positiveButtonText, this)
                    .setNegativeButton(negativeButtonText, this)
                    .create()

                alert.setOnShowListener {
                    AppLogger.debugLogs("dialog show =====", "dialog show =====")

                    val btn = alert.getButton(AlertDialog.BUTTON_POSITIVE)
                    btn.setTextColor(activity.resources.getColor(R.color.md_green_500))
                    btn.typeface = Typeface.DEFAULT_BOLD
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(activity.resources.getColor(R.color.md_grey_700));
                }
                if (title.isEmpty()) {

                    alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    binding.txtMessage.typeface = Typeface.DEFAULT_BOLD
                    binding.txtMessage.gravity = Gravity.LEFT
                }else{
                    val titleSpan = SpannableString(title)
                    titleSpan.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        titleSpan.length,
                        0
                    )
                    alert.setTitle(titleSpan)
                }
                alert.show()
            }



        }

        override fun onClick(dialog: DialogInterface?, which: Int) {
            dialog!!.dismiss()
            callback(which, positiveButtonText)
        }


}