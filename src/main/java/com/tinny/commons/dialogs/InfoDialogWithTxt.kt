package com.tinny.commons.dialogs

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R

class InfoDialogWithTxt (
    val activity: AppCompatActivity,
    message: String,
    title: String,
    positiveButtonText: String = activity.getString(R.string.ok),
    val callback: (value:Int) -> Unit
) : DialogInterface.OnClickListener {

    init {
        AlertDialog.Builder(activity)
            .setPositiveButton(positiveButtonText, this)
            .setMessage(message)
            .setTitle(title)
            .create().show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
        callback(which)
    }
}