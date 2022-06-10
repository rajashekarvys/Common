package com.tinny.commons.dialogs

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R

class OkCancacelDialog(
    val activity: AppCompatActivity,
    val message: String,
    val title: String,
    val callback: (value:Int) -> Unit
) : DialogInterface.OnClickListener {

    init {
        AlertDialog.Builder(activity)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.cancel, this)
            .setMessage(message)
            .setTitle(title)
            .create().show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
        callback(which)
    }

}