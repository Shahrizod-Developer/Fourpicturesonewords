package uz.gita.fourpicturesonewords

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import uz.gita.fourpicturesonewords.model.BackMusic
import uz.gita.fourpicturesonewords.model.MusicStart
import uz.gita.fourpicturesonewords.utils.MySharedPreference
import java.util.*

class MainActivity : AppCompatActivity() {

    private var sharedPreference: MySharedPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        fullScreenCall()

        sharedPreference = MySharedPreference.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        if (sharedPreference?.music == true) {
            BackMusic.create(this)
            BackMusic.mediaPlayer.isLooping = true
        } else {
            BackMusic.mediaPlayer.pause()
        }


        when (sharedPreference?.language) {
            "eng" -> {
                setLocate("eng")
            }
            "ru" -> {
                setLocate("ru")
            }
            "uz" -> {
                setLocate("uz")
            }
        }
    }


    private fun setLocate(language: String) {

        val resources: Resources = resources
        val metric: DisplayMetrics = resources.displayMetrics
        val configuration = resources.configuration
        configuration.locale = Locale(language)
        resources.updateConfiguration(configuration, metric)
        onConfigurationChanged(configuration)
        sharedPreference?.language = language
    }

    override fun onPause() {
        super.onPause()
        BackMusic.mediaPlayer.pause()
    }

    private fun fullScreenCall() {
        if (Build.VERSION.SDK_INT in 12..18) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            val uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }
}