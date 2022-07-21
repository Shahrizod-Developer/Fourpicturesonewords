package uz.gita.fourpicturesonewords.screen

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.gita.fourpicturesonewords.R
import uz.gita.fourpicturesonewords.contact.MainScreenContract
import uz.gita.fourpicturesonewords.databinding.InfoDialogBinding
import uz.gita.fourpicturesonewords.databinding.LanguageDialogBinding
import uz.gita.fourpicturesonewords.databinding.MainScreenBinding
import uz.gita.fourpicturesonewords.databinding.SettingsDialogBinding
import uz.gita.fourpicturesonewords.impl.main.MainModelImpl
import uz.gita.fourpicturesonewords.impl.main.MainPresenterImpl
import uz.gita.fourpicturesonewords.model.BackMusic
import uz.gita.fourpicturesonewords.model.MusicStart
import uz.gita.fourpicturesonewords.utils.MySharedPreference
import java.util.*


class MainScreen : Fragment(), MainScreenContract.View {
    private lateinit var binding: MainScreenBinding
    private lateinit var presenter: MainScreenContract.Presenter
    private var sharedPreference: MySharedPreference? = null
    private lateinit var buttonsSound: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = MainScreenBinding.inflate(inflater, container, false)
        sharedPreference = MySharedPreference.getInstance(requireContext())
        presenter = MainPresenterImpl(MainModelImpl(), this, requireContext())
        (presenter as MainPresenterImpl).init()
        buttonsSound = MusicStart.startMusic(requireContext(), R.raw.onclicks)

        coinCount()

        binding.play.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            (presenter as MainPresenterImpl).onClickPlay(
                requireView()
            )
        }
        binding.setting.setOnClickListener {
            settingDialog()
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }

        }
        binding.exit.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }

            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)

        }

        return binding.root
    }

    override fun coinCount() {
        binding.coinsCount.text = sharedPreference?.coinCount.toString()
    }

    override fun loadImageCurrentLevel(
        resIdOne: Int,
        resIdTwo: Int,
        resIdThree: Int,
        resIdFour: Int
    ) {
        binding.imageOne.setImageResource(resIdOne)
        binding.imageTwo.setImageResource(resIdTwo)
        binding.imageThree.setImageResource(resIdThree)
        binding.imageFour.setImageResource(resIdFour)
    }

    override fun currentLevel(level: Int) {
        binding.level.text = level.toString()
    }

    override fun settingDialog() {

        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding =
            SettingsDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.musicSwitch.isChecked = BackMusic.mediaPlayer.isPlaying
        dialogBinding.musicSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                BackMusic.create(requireContext())
                sharedPreference?.music = true
            } else {
                sharedPreference?.music = false
                BackMusic.mediaPlayer.pause()
            }
        }
        dialogBinding.info.setOnClickListener {
            infoDialog()
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            dialog.cancel()
        }
        dialogBinding.musicSwitch.isChecked = sharedPreference?.music!!

        dialogBinding.soundSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreference?.sound = isChecked
        }

        dialogBinding.soundSwitch.isChecked = sharedPreference?.sound!!


        dialogBinding.lang.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            langDialog()
            dialog.cancel()
        }
        dialogBinding.cancel.setOnClickListener {

            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            dialog.cancel()
        }
        dialog.setView(dialogBinding.root)
        dialog.show()
    }



    override fun infoDialog() {

        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding =
            InfoDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.yes.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            dialog.cancel()
        }
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    override fun langDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding =
            LanguageDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setCancelable(false)
        when (sharedPreference?.language) {
            "uz" -> {
                dialogBinding.uzImage.visibility = View.VISIBLE
                dialogBinding.ruImage.visibility = View.GONE
                dialogBinding.engImage.visibility = View.GONE
            }
            "ru" -> {
                dialogBinding.uzImage.visibility = View.GONE
                dialogBinding.ruImage.visibility = View.VISIBLE
                dialogBinding.engImage.visibility = View.GONE
            }
            else -> {
                dialogBinding.engImage.visibility = View.VISIBLE
                dialogBinding.ruImage.visibility = View.GONE
                dialogBinding.uzImage.visibility = View.GONE
            }
        }

        dialogBinding.eng.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            val language = "eng"
            setLocate(language)
            dialogBinding.engImage.visibility = View.VISIBLE
            dialogBinding.ruImage.visibility = View.GONE
            dialogBinding.uzImage.visibility = View.GONE
            dialogBinding.engText.text = "English"
            dialogBinding.uzText.text = "Uzbek"
            dialogBinding.ruText.text = "Russian"
        }
        dialogBinding.ru.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            val language = "ru"
            setLocate(language)
            dialogBinding.uzImage.visibility = View.GONE
            dialogBinding.ruImage.visibility = View.VISIBLE
            dialogBinding.engImage.visibility = View.GONE
            dialogBinding.engText.text = "Английский"
            dialogBinding.uzText.text = "Узбекский"
            dialogBinding.ruText.text = "Русский"
        }
        dialogBinding.uz.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            val language = "uz"
            setLocate(language)
            dialogBinding.uzImage.visibility = View.VISIBLE
            dialogBinding.ruImage.visibility = View.GONE
            dialogBinding.engImage.visibility = View.GONE
            dialogBinding.engText.text = "Inglizcha"
            dialogBinding.uzText.text = "O'zbekcha"
            dialogBinding.ruText.text = "Ruscha"
        }

        dialogBinding.cancel.setOnClickListener {
            if (sharedPreference?.sound == true) {
                buttonsSound.start()
            } else {
                buttonsSound.pause()
            }
            dialog.cancel()
            onStart()
        }
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    private fun setLocate(language: String) {

        val resources: Resources = resources
        val metric: DisplayMetrics = resources.displayMetrics
        val configuration = resources.configuration
        configuration.locale = Locale(language)
        resources.updateConfiguration(configuration, metric)
        onConfigurationChanged(configuration)
        MySharedPreference.mySharedPreference?.language = language
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        binding.text.text = requireContext().resources.getString(R.string.game_name)
        binding.play.text = requireContext().resources.getString(R.string.play)
        binding.exit.text = requireContext().resources.getString(R.string.exit)
    }
}