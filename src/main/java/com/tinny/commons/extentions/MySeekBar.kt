package com.tinny.commons.extentions

import android.widget.SeekBar

fun SeekBar.onProgressChange(cb: (Int) -> Unit){
    this.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            cb(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    })
}