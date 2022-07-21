package uz.gita.fourpicturesonewords.impl.main

import uz.gita.fourpicturesonewords.contact.MainScreenContract
import uz.gita.fourpicturesonewords.data.Database
import uz.gita.fourpicturesonewords.model.Pictures


class MainModelImpl : MainScreenContract.Repository {
    private val pictures: ArrayList<Pictures> = Database.instance!!.allQuestions
    override fun getQuestion(level: Int): Pictures {
        return pictures[level]
    }
}
