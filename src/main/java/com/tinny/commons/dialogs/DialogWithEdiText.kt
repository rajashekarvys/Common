package com.tinny.commons.dialogs

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R
import com.tinny.commons.databinding.DailogEdittextBinding
import com.tinny.commons.databinding.WatchVideoBinding

class DialogWithEdiText(
    val activity: AppCompatActivity,
    val message: String,
    val title: String,
    val callback: (value:Int,name:String) -> Unit
) : DialogInterface.OnClickListener {
    private var binding: DailogEdittextBinding =
        DailogEdittextBinding.inflate(LayoutInflater.from(activity),null,false)

    init {
        binding.edt.setText(message)
        AlertDialog.Builder(activity)
            .setView(binding.root)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.cancel, this)
            .setTitle(title)
            .create().show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
        callback(which,binding.edt.text.toString())
    }

}