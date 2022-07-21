package uz.gita.fourpicturesonewords.impl.game

import uz.gita.fourpicturesonewords.contact.GameScreenContract
import uz.gita.fourpicturesonewords.data.Database
import uz.gita.fourpicturesonewords.model.Pictures


class ModelImpl : GameScreenContract.Model {
    private val pictures: ArrayList<Pictures> = Database.instance!!.allQuestions
    override operator fun get(level: Int): Pictures {
        return pictures[level]
    }

    override val allLevel: ArrayList<Pictures>
        get() = pictures

}
