package com.tinny.commons.dialogs

import android.R
import android.app.Activity
import android.content.DialogInterface
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.tinny.commons.helper.LocalUtils


class LanguageDialog(val activity: Activity, title: String = "Test") : DialogInterface.OnClickListener {


    /*
    *
    *
    *
    * */
    val list = ArrayList<InfoData>()

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
    }

    init {
        getInfoList()
        val arrayAdapter = ArrayAdapter<String>(activity, R.layout.select_dialog_singlechoice)

        for (i in list) {
            arrayAdapter.add(i.title)
        }

        /* val view = LayoutInflater.from(activity).inflate(R.layout.parentview, null)
         addViewsToParent(list, view.parentLL, activity)*/
        val builderInner = AlertDialog.Builder(activity)
            .setAdapter(arrayAdapter) { dialog, which ->
                dialog.dismiss()
                LocalUtils.setLocale(activity, list[which].description)
            }
        builderInner.show()
    }


    data class InfoData(var title: String, var description: String)

    fun getInfoList(): ArrayList<InfoData> {

        list.add(InfoData("English", "en"))
        list.add(InfoData("German", "de"))
        list.add(InfoData("Spanish", "es"))
        list.add(InfoData("Arab", "ar"))
        list.add(InfoData("French", "fr"))
        list.add(InfoData("Hindi", "hi"))
        list.add(InfoData("Italy", "it"))
        list.add(InfoData("Japan", "ja"))
        list.add(InfoData("Korean", "ko"))
        list.add(InfoData("Russian", "ru"))
        return list
    }

}
