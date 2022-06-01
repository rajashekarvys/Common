package com.tinny.commons.helper

import android.content.Context
import android.media.MediaPlayer

object PlayAudio {
    private var mediaPlayer: MediaPlayer? = null
    private var currentFile = 0
    fun playAudio(context: Context, file: Int) {
        if (currentFile != file) {
            currentFile = file
            mediaPlayer = MediaPlayer.create(context, file)
        }

        mediaPlayer?.start()
    }

    fun stopAudio() {
        mediaPlayer?.release()
    }
}