package com.tinny.commons.dialogs

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.tinny.commons.R
import kotlinx.android.synthetic.main.progress_dialog.view.*

class ProgressDialog(activity: Activity, message: String =activity.getString(R.string.please_wait)) {

    var  alertDialog: AlertDialog
    init {

        val view = LayoutInflater.from(activity).inflate(R.layout.progress_dialog, null)
        view.txtPleaseWait.text = message
        val dialog  = AlertDialog.Builder(activity)
        dialog.setView(view)
        dialog.setCancelable(false)
        alertDialog = dialog.create()
        alertDialog.show()
    }
     fun dismiss(){
         alertDialog.dismiss()
     }


}