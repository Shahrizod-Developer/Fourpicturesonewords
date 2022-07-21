package uz.gita.fourpicturesonewords.screen

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.fourpicturesonewords.R
import uz.gita.fourpicturesonewords.adapter.EpisodeAdapter
import uz.gita.fourpicturesonewords.contact.GameScreenContract
import uz.gita.fourpicturesonewords.databinding.*
import uz.gita.fourpicturesonewords.impl.game.ModelImpl
import uz.gita.fourpicturesonewords.impl.game.PresenterImpl
import uz.gita.fourpicturesonewords.model.BackMusic
import uz.gita.fourpicturesonewords.model.MusicStart
import uz.gita.fourpicturesonewords.utils.MySharedPreference


class GameScreen : Fragment(), GameScreenContract.View {
    private lateinit var binding: GameScreenBinding
    private val answerButtons: MutableList<Button> = ArrayList()
    private val variantButtons: MutableList<Button> = ArrayList()
    private lateinit var presenter: GameScreenContract.Presenter
    private lateinit var adapter: EpisodeAdapter
    private lateinit var sharedPreference: MySharedPreference
    private lateinit var buttonSound: MediaPlayer

    private var model: ModelImpl? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameScreenBinding.inflate(inflater, container, false)

        buttonSound = MusicStart.startMusic(requireContext(), R.raw.onclicks)
        presenter = PresenterImpl(this, ModelImpl(), requireContext())

        sharedPreference = MySharedPreference.getInstance(requireContext())!!
        loadButtons(answerButtons, R.id.layout_answer, object : OnClickListener {
            override fun onClick(index: Int) {
                (presenter as PresenterImpl).onClickAnswer(index)
            }

        })

        loadButtons(variantButtons, R.id.variant_one, object : OnClickListener {
            override fun onClick(index: Int) {
                (presenter as PresenterImpl).onClickVariant(index)
            }
        })

        loadButtons(variantButtons, R.id.variant_two, object : OnClickListener {
            override fun onClick(index: Int) {
                (presenter as PresenterImpl).onClickVariant(index)
            }

        })

        (presenter as PresenterImpl).init(sharedPreference.level + 1)
        binding.back.setOnClickListener {
            (presenter as PresenterImpl).onClickBackButton(
                requireView()
            )
        }

        binding.delete.setOnClickListener {
            removeDialog()
        }
        binding.menu.setOnClickListener {
            (presenter as PresenterImpl).onClickMenuButton()
        }
        return binding.root
    }

    private fun loadButtons(buttons: MutableList<Button>, groupId: Int, listener: OnClickListener) {
        val group: ViewGroup = binding.root.findViewById(groupId)
        val oldSize = buttons.size
        for (i in 0 until group.childCount) {
            val view = group.getChildAt(i)
            if (view is Button) {
                val button = view
                val index = oldSize + i
                button.setOnClickListener { v: View? ->
                    listener.onClick(index)
                }
                buttons.add(button)
            }
        }
    }

    override fun levelState(index: Int) {
        binding.level.text = index.toString()
    }

    override fun coinState(coinCount: Int) {
        binding.coinsCount.text = coinCount.toString()
    }

    override fun loadImages(resIdOne: Int, resIdTwo: Int, resIdThree: Int, resIdFour: Int) {
        with(binding) {
            this.imageOneS.setImageResource(resIdOne)
            imageTwoS.setImageResource(resIdTwo)
            imageThreeS.setImageResource(resIdThree)
            imageFourS.setImageResource(resIdFour)
        }
    }

    override fun hideAnswer(index: Int) {
        answerButtons[index].visibility = View.GONE
    }

    override fun showAnswer(index: Int) {
        answerButtons[index].visibility = View.VISIBLE
    }

    override fun clearAnswer(index: Int) {
        answerButtons[index].text = ""
    }

    override fun writeAnswer(index: Int, text: String?) {
        answerButtons[index].text = text
    }

    override fun writeVariant(index: Int, text: String?) {
        variantButtons[index].text = text
    }

    override fun showVariant(index: Int) {
        variantButtons[index].visibility = View.VISIBLE
    }

    override fun hideVariant(index: Int) {
        variantButtons[index].visibility = View.INVISIBLE
    }

    override fun isShownVariant(index: Int): Boolean {
        return variantButtons[index].visibility == View.VISIBLE
    }

    override fun removeDialog() {

        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding =
            DeleteDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.setCancelable(false)

        dialogBinding.yes.setOnClickListener { it ->
            presenter.onYesClick()
            binding.delete.alpha = 0.5f
            binding.delete.isClickable = false

            dialog.cancel()
        }

        dialogBinding.cancel.setOnClickListener { v ->

            if (sharedPreference.sound) {
                buttonSound.start()
            } else {
                buttonSound.pause()
            }
            dialog.cancel()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    override fun warningDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding =
            WarningDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.setCancelable(false)

        dialogBinding.yes.setOnClickListener { it ->
            if (sharedPreference.sound) {
                buttonSound.start()
            } else {
                buttonSound.pause()
            }
            dialog.cancel()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    override fun winDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding =
            WinDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.setCancelable(false)

        dialogBinding.yes.setOnClickListener { it ->
            findNavController().navigate(R.id.action_gameScreen_to_mainScreen)


            if (sharedPreference.music) {
                BackMusic.mediaPlayer.start()
            } else {
                BackMusic.mediaPlayer.pause()
            }
            dialog.cancel()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    override fun langState(language: String) {
        binding.lang.text = language
    }

    internal interface OnClickListener {
        fun onClick(index: Int)
    }

    override fun episodeDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding: EpisodeDialogBinding = EpisodeDialogBinding.inflate(
            LayoutInflater.from(requireContext()),
            binding.root,
            false
        )

        model = ModelImpl()
        adapter = EpisodeAdapter(model?.allLevel!!, requireContext()) { _, pos ->
            sharedPreference.itemClicked = true
            binding.delete.alpha = 1f
            binding.delete.isClickable = true
            presenter.init(pos)
            dialog.cancel()
        }
        dialogBinding.rv.adapter = adapter
        dialogBinding.rv.smoothScrollToPosition(sharedPreference.level)
        dialog.setCancelable(false)
        dialogBinding.cancel.setOnClickListener { v ->
            if (sharedPreference.sound){
                buttonSound.start()
            }
            else{
                buttonSound.pause()
            }
            dialog.cancel()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    override fun resultDialog(level: Int, coinCount: Int, answer: String?) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding: ResultDialogBinding =
            ResultDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        model = ModelImpl()
        dialog.setCancelable(false)
        dialogBinding.answers.text = answer
        dialogBinding.coinsCount.text = coinCount.toString() + ""
        dialogBinding.continueGame.setOnClickListener {
            presenter.onClickContinue()
            dialog.cancel()
            binding.delete.alpha = 1f
            binding.delete.isClickable = true
        }
        dialog.setView(dialogBinding.root)
        dialog.show()
    }
}