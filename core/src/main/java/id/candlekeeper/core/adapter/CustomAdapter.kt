package id.candlekeeper.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.nativead.NativeAd
import com.startapp.sdk.ads.nativead.NativeAdDetails
import id.candlekeeper.core.adapter.viewHolder.*
import id.candlekeeper.core.databinding.*
import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.AdapterUtils
import id.candlekeeper.core.utils.OnItemClicked
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.core.utils.function.populateNativeAdView

class CustomAdapter(
    private val onItemClicked: OnItemClicked,
    private val prefManager: PrefManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = mutableListOf<Any>()

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
            is Endorse -> {
                AdapterUtils.ENDORSE_VIEW
            }
            is UrlTutorial -> {
                AdapterUtils.MENU_ITEM_URL_TUTOR
            }
            is Heroes -> {
                AdapterUtils.MENU_ITEM_HEROES
            }
            is Skins -> {
                AdapterUtils.MENU_ITEM_SKINS
            }
            else -> AdapterUtils.MENU_ITEM_SKINS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AdapterUtils.ADD_VIEW_ADMOB -> {
                val menuLayout = ItemAdNativeListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AdsViewHolderList(menuLayout)
            }
            AdapterUtils.ADD_VIEW_STARTIO -> {
                val menuLayout = ItemAdNativeStartioBigBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AddViewStartIoHolder(menuLayout)
            }
            AdapterUtils.ADD_VIEW_APPLOVIN -> {
                val menuLayout =
                    ItemAdViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AddViewAppLovinHolder(menuLayout)
            }
            AdapterUtils.ENDORSE_VIEW -> {
                val menuLayout = ItemEndorseListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EndorseViewHolderList(parent.context, menuLayout)
            }
            AdapterUtils.MENU_ITEM_URL_TUTOR -> {
                val menuLayout =
                    ItemTutorialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UrlTutorialViewHolder(menuLayout)
            }
            AdapterUtils.MENU_ITEM_HEROES -> {
                val menuLayout =
                    ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeroViewHolder(parent.context, menuLayout)
            }
            AdapterUtils.MENU_ITEM_SKINS -> {
                val menuLayout =
                    ItemSkinsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SkinsViewHolder(menuLayout)
            }
            else -> {
                val menuLayout =
                    ItemSkinsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SkinsViewHolder(menuLayout)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            AdapterUtils.ADD_VIEW_ADMOB -> {
                val menuItemHolder = (holder as AdsViewHolderList).adView
                populateNativeAdView(list[position] as NativeAd, menuItemHolder)
            }
            AdapterUtils.ADD_VIEW_STARTIO -> {
                val menuItemHolder = holder as AddViewStartIoHolder
                menuItemHolder.bind(list[position] as NativeAdDetails)
            }
            AdapterUtils.ADD_VIEW_APPLOVIN -> {
                val menuItemHolder = holder as AddViewAppLovinHolder
                menuItemHolder.bind(list[position] as MaxNativeAdView)
            }
            AdapterUtils.ENDORSE_VIEW -> {
                val menuItemHolderList = holder as EndorseViewHolderList
                menuItemHolderList.bind(onItemClicked, list[position] as Endorse, prefManager)
            }
            AdapterUtils.MENU_ITEM_URL_TUTOR -> {
                val menuItemHolder = holder as UrlTutorialViewHolder
                menuItemHolder.bind(onItemClicked, list[position] as UrlTutorial)
            }
            AdapterUtils.MENU_ITEM_HEROES -> {
                val menuItemHolder = holder as HeroViewHolder
                menuItemHolder.bind(onItemClicked, list[position] as Heroes, prefManager)
            }
            AdapterUtils.MENU_ITEM_SKINS -> {
                val menuItemHolder = holder as SkinsViewHolder
                menuItemHolder.bind(onItemClicked, list[position] as Skins, prefManager)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
