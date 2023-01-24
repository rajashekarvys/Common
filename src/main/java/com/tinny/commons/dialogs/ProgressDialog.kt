package com.tinny.commons.dialogs

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.tinny.commons.R
import com.tinny.commons.databinding.AtingDialogBinding
import com.tinny.commons.databinding.ProgressDialogBinding

class ProgressDialog(activity: Activity, message: String =activity.getString(R.string.please_wait)) {

    var  alertDialog: AlertDialog
    init {
        var binding: ProgressDialogBinding =
            ProgressDialogBinding.inflate(LayoutInflater.from(activity),null,false)

        binding.txtPleaseWait.text = message
        val dialog  = AlertDialog.Builder(activity)
        dialog.setView(binding.root)
        dialog.setCancelable(false)
        alertDialog = dialog.create()
        alertDialog.show()
    }
     fun dismiss(){
         alertDialog.dismiss()
     }


}