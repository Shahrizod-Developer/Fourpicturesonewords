package uz.gita.fourpicturesonewords.data

import uz.gita.fourpicturesonewords.R
import uz.gita.fourpicturesonewords.model.Pictures


class Database private constructor() {
    val allQuestions: ArrayList<Pictures>
        get() {
            val pictures: ArrayList<Pictures> = ArrayList<Pictures>()
            pictures.add(
                Pictures(
                    R.drawable.ball_one,
                    R.drawable.ball_two,
                    R.drawable.ball_three,
                    R.drawable.ball_four,
                    "ball",
                    "lmrvuxntlbshak",
                    "мяч",
                    "щвамвалопяыачи",
                    "to'p",
                    "sydhstsdj'dgop"
                )
            )
            pictures.add(
                Pictures(
                    R.drawable.tea_one,
                    R.drawable.tea_two,
                    R.drawable.tea_three,
                    R.drawable.tea_four,
                    "tea",
                    "phtaegjxrokivf",
                    "чай",
                    "еецвчцкаиыйврв",
                    "choy",
                    "csdhfhdgosgyer"
                )
            )
            pictures.add(
                Pictures(
                    R.drawable.image11,
                    R.drawable.image12,
                    R.drawable.image13,
                    R.drawable.image14,
                    "apple",
                    "phtpaeglxrokiv",
                    "яблоко",
                    "лсоекцояаицьубц",
                    "olma",
                    "qmmswaoasdlcds"
                )
            )
            pictures.add(
                Pictures(
                    R.drawable.image31,
                    R.drawable.image32,
                    R.drawable.image33,
                    R.drawable.image34,
                    "shadow",
                    "ssiiwdthstahho",
                    "тень",
                    "эеыьтдтаболаын",
                    "soya",
                    "huaotdsbiyvek'"
                )
            )

            pictures.add(
                Pictures(
                    R.drawable.image41,
                    R.drawable.image42,
                    R.drawable.image43,
                    R.drawable.image44,
                    "window",
                    "pbstionshdwcwi",
                    "окно",
                    "мйэнлоттыжекбьо",
                    "deraza",
                    "quebiaarlcmzdu"
                )
            )

            pictures.add(
                Pictures(
                    R.drawable.image61,
                    R.drawable.image62,
                    R.drawable.image63,
                    R.drawable.image64,
                    "mouse",
                    "ouzimdgewxabvs",
                    "мышь",
                    "етиаодмудшбыгь",
                    "sichqon",
                    "iloqrnxgdhaasc"
                )
            )

            pictures.add(
                Pictures(
                    R.drawable.image91,
                    R.drawable.image92,
                    R.drawable.image93,
                    R.drawable.image94,
                    "italy",
                    "wtualyhgonioey",
                    "италия",
                    "лясирщпаоятимю",
                    "italiya",
                    "aouxaixbyljiyt"
                )
            )

            return pictures
        }

    companion object {
        private var database: Database? = null
        val instance: Database?
            get() {
                if (database == null) {
                    database = Database()
                }
                return database
            }
    }
}
