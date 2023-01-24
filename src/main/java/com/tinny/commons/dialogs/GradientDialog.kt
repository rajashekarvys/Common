package com.tinny.commons.dialogs

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.tinny.commons.R
import com.tinny.commons.databinding.DailogEdittextBinding
import com.tinny.commons.databinding.ListDialogBinding
import com.tinny.commons.helper.UtilsHelper
import com.tinny.commons.views.MyGridLayoutManager

class GradientDialog(var activity: Activity, title: String) : DialogInterface.OnClickListener {

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialog!!.dismiss()
    }
    private var binding: ListDialogBinding =
        ListDialogBinding.inflate(LayoutInflater.from(activity),null,false)

    init {
       val list = loadArray()
        val view = LayoutInflater.from(activity).inflate(R.layout.list_dialog, null)
        binding.rcvDialog.layoutManager =  MyGridLayoutManager(activity,3)
        binding.rcvDialog.adapter = GradientAdapter(list)
        AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(activity.getString(R.string.cancel), this)
                .setTitle(title)
                .create().show()
    }

    private fun loadArray(): ArrayList<GradientData> {
        val list = ArrayList<GradientData>()
        list.add(GradientData(activity.getString(R.string.blue), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#de6262"), Color.parseColor("#ffb88c"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.blue), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#de6262"), Color.parseColor("#ffb88c"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.blue), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#de6262"), Color.parseColor("#ffb88c"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.blue), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#de6262"), Color.parseColor("#ffb88c"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.blue), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#de6262"), Color.parseColor("#ffb88c"))))
        list.add(GradientData(activity.getString(R.string.red), intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed"))))

        return list
    }
}


data class GradientData(var title: String, var colorArray:IntArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GradientData

        if (title != other.title) return false
        if (!colorArray.contentEquals(other.colorArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + colorArray.contentHashCode()
        return result
    }
}


class GradientHolder(view:View):RecyclerView.ViewHolder(view)
class GradientAdapter(var data: ArrayList<GradientData>): RecyclerView.Adapter<GradientHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradientHolder {
        return GradientHolder(LayoutInflater.from(parent.context).inflate(R.layout.gradient_row,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: GradientHolder, position: Int) {
        /*holder.itemView.gradientTxt.background = UtilsHelper.getGerdientBackGround(data[position].colorArray)
        holder.itemView.gradientCaption.text = data[position].title*/
    }

}
