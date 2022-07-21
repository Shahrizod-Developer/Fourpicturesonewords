package uz.gita.fourpicturesonewords.model

import android.content.Context
import android.media.MediaPlayer

object MusicStart {

    private lateinit var mediaPlayer: MediaPlayer

    fun startMusic(context: Context, resInt: Int): MediaPlayer{
        mediaPlayer= MediaPlayer.create(context, resInt)
        return mediaPlayer
    }
}