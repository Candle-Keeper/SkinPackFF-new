package id.candlekeeper.core.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import id.candlekeeper.core.R
import id.candlekeeper.core.databinding.ItemHeroBinding
import id.candlekeeper.core.databinding.ItemSkinsBinding
import id.candlekeeper.core.databinding.ItemTutorialBinding
import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.utils.*


class UrlTutorialViewHolder(private val binding: ItemTutorialBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(onItemClicked: OnItemClicked, data: UrlTutorial) {
        with(binding) {
            tvTutorial.text = data.name
            ivTutorial.loadImageYt(data.youtubeUrl!!)
        }
        binding.root.setOnClickListener {
            onItemClicked.onEventClick(data)
        }
    }
}

@SuppressLint("SetTextI18n")
class HeroViewHolder(private val context: Context, private val binding: ItemHeroBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(onItemClicked: OnItemClicked, data: Heroes, prefManager: PrefManager) {
        with(binding) {
            tvName.text = data.name
            tvDescription.text =
                context.resources.getString(R.string.pilih_skins) + " ${data.name} \n" + context.resources.getString(
                    R.string.kamu_disini
                )
            imgBackground.loadImage(prefManager, data.image_url.toString())
        }
        binding.root.setOnClickListener {
            onItemClicked.onEventClick(data)
        }
    }
}

class SkinsViewHolder(private val binding: ItemSkinsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(onItemClicked: OnItemClicked, data: Skins, prefManager: PrefManager) {
        with(binding) {
            tvTitle.text = data.name
            imgBackground.loadImage(prefManager, data.imageUrl.toString())
            tvTypeScript.let {
                if (data.isMax!!) it.text = "Max" else it.hide()
            }
        }
        binding.root.setOnClickListener {
            onItemClicked.onEventClick(data)
        }
    }
}