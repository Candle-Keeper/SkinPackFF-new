package id.candlekeeper.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.nativead.NativeAd
import com.startapp.sdk.ads.nativead.NativeAdDetails
import id.candlekeeper.core.adapter.viewHolder.AddViewAppLovinCarouselHolder
import id.candlekeeper.core.adapter.viewHolder.AddViewStartIoCarouselHolder
import id.candlekeeper.core.adapter.viewHolder.AdsViewHolderCarousel
import id.candlekeeper.core.databinding.ItemAdNativeCarouselBinding
import id.candlekeeper.core.databinding.ItemAdNativeStartioCarouselBinding
import id.candlekeeper.core.databinding.ItemAdViewCarouselBinding
import id.candlekeeper.core.databinding.ItemCarouselBinding
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.populateNativeAdView

class CarouselAdapter(
    private val onItemClicked: OnItemClicked,
    private val prefManager: PrefManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list = mutableListOf<Any>()

    fun addList(listData: List<Any>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            DiffCallback(
                list,
                listData
            )
        )
        list.clear()
        list.addAll(listData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is NativeAd -> {
                AdapterUtils.ADD_VIEW_ADMOB
            }
            is NativeAdDetails -> {
                AdapterUtils.ADD_VIEW_STARTIO
            }
            is MaxNativeAdView -> {
                AdapterUtils.ADD_VIEW_APPLOVIN
            }
            else -> AdapterUtils.MENU_ITEM_CAROUSEL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterUtils.ADD_VIEW_ADMOB -> {
                val menuLayout = ItemAdNativeCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AdsViewHolderCarousel(menuLayout)
            }
            AdapterUtils.ADD_VIEW_STARTIO -> {
                val menuLayout = ItemAdNativeStartioCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddViewStartIoCarouselHolder(menuLayout)
            }
            AdapterUtils.ADD_VIEW_APPLOVIN -> {
                val menuLayout = ItemAdViewCarouselBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddViewAppLovinCarouselHolder(menuLayout)
            }
            AdapterUtils.MENU_ITEM_CAROUSEL -> {
                val binding =
                    ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyViewHolder(onItemClicked, binding)
            }
            else -> {
                val binding =
                    ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MyViewHolder(onItemClicked, binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            AdapterUtils.ADD_VIEW_ADMOB -> {
                val nativeAd: NativeAd = list[position] as NativeAd
                populateNativeAdView(nativeAd, (holder as AdsViewHolderCarousel).adView)
            }
            AdapterUtils.ADD_VIEW_STARTIO -> {
                val menuItemHolder = holder as AddViewStartIoCarouselHolder
                menuItemHolder.bind(list[position] as NativeAdDetails)
            }
            AdapterUtils.ADD_VIEW_APPLOVIN -> {
                val menuItemHolder = holder as AddViewAppLovinCarouselHolder
                menuItemHolder.bind(list[position] as MaxNativeAdView)
            }
            AdapterUtils.MENU_ITEM_CAROUSEL -> {
                val menuItemHolder = holder as MyViewHolder
                menuItemHolder.bind(onItemClicked, list[position] as Carousel)
            }
            else -> {
                val menuItemHolder = holder as MyViewHolder
                menuItemHolder.bind(onItemClicked, list[position] as Carousel)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class MyViewHolder(
        val onItemClicked: OnItemClicked,
        private val binding: ItemCarouselBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onItemClicked: OnItemClicked, data: Carousel) {
            with(binding) {
                tvName.text = data.name
                ivAvatar.loadImageFull(prefManager, data.imageUrl.toString())
            }
            binding.root.setOnClickListener {
                onItemClicked.onEventClick(data)
            }
        }
    }
}
