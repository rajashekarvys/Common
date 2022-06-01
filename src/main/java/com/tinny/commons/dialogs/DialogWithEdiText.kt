package com.tinny.commons.dialogs

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R
import kotlinx.android.synthetic.main.dailog_edittext.view.*

class DialogWithEdiText(
    val activity: AppCompatActivity,
    val message: String,
    val title: String,
    val callback: (value:Int,name:String) -> Unit
) : DialogInterface.OnClickListener {
    val view: View = LayoutInflater.from(activity).inflate(R.layout.dailog_edittext, null)

    init {
        view.edt.setText(message)
        AlertDialog.Builder(activity)
            .setView(view)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.cancel, this)
            .setTitle(title)
            .create().show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
        callback(which,view.edt.text.toString())
    }

}