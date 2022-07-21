package uz.gita.fourpicturesonewords.adapter

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import uz.gita.fourpicturesonewords.R
import uz.gita.fourpicturesonewords.databinding.EpisodeDialogBinding
import uz.gita.fourpicturesonewords.databinding.ItemEpisodeBinding
import uz.gita.fourpicturesonewords.model.MusicStart
import uz.gita.fourpicturesonewords.model.Pictures
import uz.gita.fourpicturesonewords.utils.MySharedPreference


class EpisodeAdapter(
    var list: List<Pictures>,
    val context: Context,
    val onClick: (picture: Pictures, pos: Int) -> Unit
) :
    RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    private lateinit var itemClickSound: MediaPlayer

    inner class ViewHolder(private var binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {


        private val sharedPreference: MySharedPreference? = MySharedPreference.getInstance(context)
        fun onBind(picture: Pictures, pos: Int) {
            itemClickSound = MusicStart.startMusic(context, R.raw.item_click)
            itemView.setOnClickListener {
                onClick(picture, pos)
                if (sharedPreference?.sound == true) {
                    itemClickSound.start()
                } else {
                    itemClickSound.pause()
                }

            }
            binding.item.isClickable = true
            val level: Int = sharedPreference?.level!!
            if (pos < level) {
                binding.image.setImageResource(R.drawable.ic_baseline_check_24)
                binding.item.alpha = 1f
            } else if (pos == level) {
                binding.image.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
                binding.item.alpha = 1f
            } else {
                binding.item.isClickable = false
                binding.image.setImageResource(R.drawable.ic_baseline_lock_24)
                binding.item.alpha = 0.5f
            }
            binding.episode.text = "Level " + (pos + 1)
            binding.imageOneS.setImageResource(list[pos].picturesOne)
            binding.imageTwoS.setImageResource(list[pos].picturesTwo)
            binding.imageThreeS.setImageResource(list[pos].picturesThree)
            binding.imageFourS.setImageResource(list[pos].picturesFour)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)


    }

    override fun getItemCount(): Int = list.size
}
//
//
//class EpisodeAdapter(
//    val context: Context,
//    val list: ArrayList<Pictures>,
//    val onClick: (pictures: Pictures, pos: Int) -> Unit
//) :
//    RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            ItemEpisodeBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val sharedPreference: MySharedPreference? = MySharedPreference.getInstance(context)
//        val level: Int = sharedPreference?.level!! + 1
//        if (position < level) {
//            holder.binding.item.isClickable = true
//            holder.binding.image.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
//            holder.binding.item.alpha = 1f
//        } else {
//            holder.binding.item.isClickable = false
//            holder.binding.image.setImageResource(R.drawable.ic_baseline_lock_24)
//            holder.binding.item.alpha = 0.5f
//        }
//        holder.binding.episode.text = "Level " + (position + 1)
//        holder.binding.imageOneS.setImageResource(list[position].picturesOne)
//        holder.binding.imageTwoS.setImageResource(list[position].picturesTwo)
//        holder.binding.imageThreeS.setImageResource(list[position].picturesThree)
//        holder.binding.imageFourS.setImageResource(list[position].picturesFour)
//        holder.itemView.setOnClickListener {
//            onClick(list[position], position)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    class ViewHolder(binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {
//        var binding: ItemEpisodeBinding
//
//        init {
//            this.binding = binding
//        }
//    }
//}