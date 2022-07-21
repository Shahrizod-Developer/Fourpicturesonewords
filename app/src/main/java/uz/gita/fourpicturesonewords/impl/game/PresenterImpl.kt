package uz.gita.fourpicturesonewords.impl.game

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation.findNavController
import uz.gita.fourpicturesonewords.R
import uz.gita.fourpicturesonewords.contact.GameScreenContract
import uz.gita.fourpicturesonewords.model.*
import uz.gita.fourpicturesonewords.utils.MySharedPreference


class PresenterImpl(
    view: GameScreenContract.View,
    model: GameScreenContract.Model,
    context: Context
) :
    GameScreenContract.Presenter {
    private val context: Context
    private val model: GameScreenContract.Model
    private val view: GameScreenContract.View
    private var sharedPreference: MySharedPreference? = null
    private var level = 0
    private var gameData: Pictures? = null
    private lateinit var userAnswer: Array<String?>
    private lateinit var buttonsSound: MediaPlayer
    private lateinit var answerSound: MediaPlayer
    private lateinit var variantSound: MediaPlayer
    private lateinit var winSound: MediaPlayer
    private lateinit var coinNotEnoughSound: MediaPlayer
    private lateinit var errorSound: MediaPlayer
    private lateinit var completeSound: MediaPlayer
    private lateinit var openLevelSound: MediaPlayer
    private lateinit var itemClickSound: MediaPlayer
    private lateinit var coinSpendSound: MediaPlayer
    private lateinit var answer: String
    private lateinit var variant: String

    private var coin = 0
    override fun init(pos: Int) {

        sharedPreference = MySharedPreference.getInstance(context)
        if (sharedPreference?.itemClicked!!) {
            level = pos
            sharedPreference?.itemClicked = false
            if (pos > sharedPreference?.level!!) {
                sharedPreference?.level = pos
            }
        } else {
            level = sharedPreference!!.level
        }

        initSound()
        coin = sharedPreference!!.coinCount
        view.levelState(level + 1)
        view.coinState(coin)
        gameData = model[level]
        view.loadImages(
            gameData!!.picturesOne,
            gameData!!.picturesTwo,
            gameData!!.picturesThree,
            gameData!!.picturesFour
        )

        when (sharedPreference?.language) {
            "eng" -> {
                answer = gameData?.answerEng!!
                variant = gameData?.variantEng!!
                view.langState("English")
            }
            "ru" -> {
                answer = gameData?.answerRu!!
                variant = gameData?.variantRu!!
                view.langState("Русский")
            }
            "uz" -> {
                answer = gameData?.answerUz!!
                variant = gameData?.variantUz!!
                view.langState("O'zbekcha")
            }
        }
        val size: Int = answer.length
        for (i in 0 until GameScreenContract.MAX_ANSWER) {
            if (i < size) {
                view.showAnswer(i)
                view.clearAnswer(i)
            } else {
                view.hideAnswer(i)
            }
        }
        for (i in 0 until GameScreenContract.VARIANT_COUNT) {
            val text = getVariant(i)
            view.writeVariant(i, text)
            view.showVariant(i)
        }
        userAnswer = arrayOfNulls(answer.length)
    }

    private fun initSound() {
        buttonsSound = MusicStart.startMusic(context, R.raw.onclicks)
        answerSound = MusicStart.startMusic(context, R.raw.onclickanser)
        variantSound = MusicStart.startMusic(context, R.raw.varaint)
        winSound = MusicStart.startMusic(context, R.raw.win)
        errorSound = MusicStart.startMusic(context, R.raw.error)
        completeSound = MusicStart.startMusic(context, R.raw.completegame)
        coinNotEnoughSound = MusicStart.startMusic(context, R.raw.spendcoin)
        openLevelSound = MusicStart.startMusic(context, R.raw.openlevel)
        itemClickSound = MusicStart.startMusic(context, R.raw.item_click)
        coinSpendSound = MusicStart.startMusic(context, R.raw.spend_coin)
    }

    override fun onClickBackButton(view: View?) {
        if (sharedPreference?.sound == true) {
            buttonsSound.start()
        } else {
            buttonsSound.pause()
        }
        findNavController(view!!).navigate(R.id.action_gameScreen_to_mainScreen)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onClickDeleteButton() {

        if (coin >= 100) {

            val answers: String = answer
            val variants: String = variant
            val ansList = ArrayList<String>()
            val varList = ArrayList<String>()
            for (element in variants) {
                varList.add(element.toString())
            }
            for (element in answers) {
                ansList.add(element.toString())
            }
            for (i in varList.indices) {
                for (j in ansList.indices) {
                    if (varList[i] == ansList[j]) {
                        view.showVariant(i)
                        break
                    } else {
                        view.hideVariant(i)
                    }
                }
            }
            coin -= 100
            view.coinState(coin)
            if (sharedPreference?.sound == true) {
                coinSpendSound.start()
            } else {
                coinSpendSound.start()
            }
            sharedPreference?.coinCount = coin
        } else {
            view.warningDialog()
            sharedPreference?.coinCount = coin
            if (sharedPreference?.sound == true) {
                coinNotEnoughSound.start()
            } else {
                coinNotEnoughSound.pause()
            }

        }
    }

    override fun onClickMenuButton() {
        if (sharedPreference?.sound == true) {
            buttonsSound.start()
        } else {
            buttonsSound.pause()
        }
        view.episodeDialog()
    }

    private fun getVariant(index: Int): String {
        val variants: String = variant
        return variants[index].toString()
    }

    override fun onClickAnswer(index: Int) {


        if (sharedPreference?.sound == true) {
            answerSound.start()
        } else {
            answerSound.pause()
        }
        val text = userAnswer[index]
        if (userAnswer[index] != null) {
            for (i in 0 until GameScreenContract.VARIANT_COUNT) {
                val textVariant = getVariant(i)
                if (text == textVariant && !view.isShownVariant(i)) {
                    view.showVariant(i)
                    view.clearAnswer(index)
                    userAnswer[index] = null
                    return
                }
            }
        }
    }

    override fun onClickVariant(index: Int) {

        if (sharedPreference?.sound == true) {
            variantSound.start()
        } else {
            variantSound.pause()
        }
        val answerIndex = findFirstEmptyIndex()
        if (answerIndex != -1) {
            view.hideVariant(index)
            val text = getVariant(index)
            view.writeAnswer(answerIndex, text)
            userAnswer[answerIndex] = text
            if (findFirstEmptyIndex() == -1 && isWin) {
                coin += 10
                sharedPreference?.coinCount = coin

                if (sharedPreference?.sound == true) {
                    winSound.start()
                } else {
                    winSound.pause()
                }

                if (level == model.allLevel.size - 1) {
                    view.winDialog()
                    if (sharedPreference?.sound == true) {
                        winSound.pause()
                        BackMusic.mediaPlayer.pause()
                        completeSound.start()
                    } else {
                        completeSound.pause()
                    }

                } else {
                    view.resultDialog(level, coin, answer)
                }
            } else if (findFirstEmptyIndex() == -1 && !isWin) {

                if (sharedPreference?.sound == true) {
                    errorSound.start()
                } else {
                    errorSound.pause()
                }
            }
        }
    }

    override fun onClickContinue() {
        level++
        sharedPreference?.level = level
        init(level)

        if (sharedPreference?.sound == true) {
            openLevelSound.start()
        } else {
            openLevelSound.pause()
        }

    }

    override fun onClickImage(index: Int) {
    }

    override fun onClickFullImage() {
    }

    override fun onYesClick() {
        onClickDeleteButton()
    }

    private fun findFirstEmptyIndex(): Int {
        for (i in userAnswer.indices) {
            if (userAnswer[i] == null) {
                return i
            }
        }
        return -1
    }

    private val isWin: Boolean
        get() {
            var a: String? = ""
            for (s in userAnswer) {
                a += s
            }
            return a == answer
        }

    init {
        this.view = view
        this.model = model
        this.context = context
    }
}
