package uz.gita.fourpicturesonewords.model

import android.content.Context
import android.media.MediaPlayer
import uz.gita.fourpicturesonewords.R

object AnswerOnclick {
    var mediaPlayer = MediaPlayer()
    fun create(context: Context?) {
        mediaPlayer = MediaPlayer.create(context, R.raw.onclickanser)
        mediaPlayer.start()
    }
}