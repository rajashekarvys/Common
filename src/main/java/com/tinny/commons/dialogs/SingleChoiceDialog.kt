package com.tinny.commons.dialogs

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/*SingleChoiceDialog(this, arrayOf("1 Sec", "2 Sec", "3 sec", "4 Sec", "5 Sec"), "", 1) {
    slideShow = true
    handler = Handler()
    startSlideShow(it)
    hideSystemUI()
}*/

class SingleChoiceDialog(
    val activity: AppCompatActivity,
    items: Array<String>,
    val title: String,
    val selected:Int,
    val callback: (value: Int) -> Unit
) : DialogInterface.OnClickListener {

    override fun onClick(dialog: DialogInterface?, which: Int) {
        callback((dialog as AlertDialog).listView.checkedItemPosition)
        dialog.dismiss()
    }

    init {
        AlertDialog.Builder(activity).setSingleChoiceItems(items, selected, this).show()
    }
}