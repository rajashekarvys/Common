package com.tinny.commons.dialogs

import android.app.Activity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.tinny.commons.R
import kotlinx.android.synthetic.main.parentview.view.*
import kotlinx.android.synthetic.main.about_row.view.*

class InfoDialog(activity: Activity, list: ArrayList<InfoData>, title: String) : DialogInterface.OnClickListener {

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
    }

    init {
        val view = LayoutInflater.from(activity).inflate(R.layout.parentview, null)
        addViewsToParent(list, view.parentLL, activity)
        AlertDialog.Builder(activity)
            .setView(view)
            .setPositiveButton(activity.getString(R.string.ok), this)
            .setTitle(title)
            .create().show()

    }
}

fun addViewsToParent(list: ArrayList<InfoData>, view: LinearLayout, activity: Activity) {
    for (data in list) {
        val childView = LayoutInflater.from(activity).inflate(R.layout.about_row, null)
        childView.txtTitle.text = data.title
        childView.txtDesc.text = data.description
        view.addView(childView)
    }
}


data class InfoData(var title: String, var description: String)