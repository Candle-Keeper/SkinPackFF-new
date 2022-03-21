package id.candlekeeper.core.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.nativead.NativeAdView
import com.startapp.sdk.ads.nativead.NativeAdDetails
import id.candlekeeper.core.databinding.*
import id.candlekeeper.core.utils.function.populateNativeStartIoBig


class AdsViewHolderList(val binding: ItemAdNativeListBinding) : ViewHolder(binding.root) {
    val adView: NativeAdView = binding.root

    init {
        adView.mediaView = binding.adMedia
        adView.headlineView = binding.adHeadline
        adView.bodyView = binding.adBody
        adView.callToActionView = binding.adCallToAction
        adView.iconView = binding.adAppIcon
        adView.priceView = binding.adPrice
        adView.starRatingView = binding.adStars
        adView.storeView = binding.adStore
        adView.advertiserView = binding.adAdvertiser
    }
}

class AddViewStartIoHolder(private val binding: ItemAdNativeStartioBigBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NativeAdDetails) {
        populateNativeStartIoBig(data, binding)
    }
}

class AddViewStartIoCarouselHolder(private val binding: ItemAdNativeStartioCarouselBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: NativeAdDetails) {
        with(binding) {
            tvTitle.text = data.title
            tvDescription.text = data.description
            if (data.imageBitmap != null) {
                ivIcon.setImageBitmap(data.imageBitmap)
            } else {
                ivIcon.setImageBitmap(data.secondaryImageBitmap)
            }
            data.registerViewForInteraction(root)
            btnInstall.text = if (data.isApp) "Install" else "Open"
            btnInstall.setOnClickListener { root.performClick() }
        }
    }
}

class AddViewAppLovinHolder(private val binding: ItemAdViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: MaxNativeAdView) {
        with(binding.root) {
            removeAllViews()
            addView(data)
        }
    }
}

class AddViewAppLovinCarouselHolder(private val binding: ItemAdViewCarouselBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: MaxNativeAdView) {
        with(binding.root) {
            removeAllViews()
            addView(data)
        }
    }
}


class AdsViewHolderCarousel(val binding: ItemAdNativeCarouselBinding) : ViewHolder(binding.root) {
    val adView: NativeAdView = binding.root

    init {
        adView.mediaView = binding.adMedia
        adView.headlineView = binding.adHeadline
        adView.bodyView = binding.adBody
        adView.callToActionView = binding.adCallToAction
        adView.iconView = binding.adAppIcon
        adView.priceView = binding.adPrice
        adView.starRatingView = binding.adStars
        adView.storeView = binding.adStore
        adView.advertiserView = binding.adAdvertiser
    }
}