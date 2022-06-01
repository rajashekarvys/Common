package com.tinny.commons.dialogs

import android.content.DialogInterface
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tinny.commons.R
import com.tinny.commons.helper.AppLogger
import kotlinx.android.synthetic.main.ating_dialog.view.*

class DialogWithRatingBar(
    val activity: AppCompatActivity,
    message: String,
    title: String,
    rating: Int,
    private val positiveButtonText: String = activity.getString(R.string.ok),
    negativeButtonText: String = activity.getString(R.string.cancel),
    val callback: (value: Int, name: String) -> Unit
    ) : DialogInterface.OnClickListener {
    val view: View = LayoutInflater.from(activity.baseContext).inflate(R.layout.ating_dialog, null)

    init {
        view.txtMessage.text = message
        view.ratingBar.rating = rating.toFloat()
        if (!activity.isDestroyed) {
            if (rating == 3) {
                Glide.with(activity)
                    .asGif()
                    .load(R.drawable.animation_300_kkpd76lz)
                    .into(view.ratingImg)
            } else {
                Glide.with(activity)
                    .asGif()
                    .load(R.drawable.animation_300_kkpd7el9)
                    .into(view.ratingImg)
            }
        }
        if (!activity.isDestroyed) {
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
        callback(which, positiveButtonText)
    }

}