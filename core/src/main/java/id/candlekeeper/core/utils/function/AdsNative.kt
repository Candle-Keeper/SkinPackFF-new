package id.candlekeeper.core.utils.function

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.startapp.sdk.ads.nativead.NativeAdDetails
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import id.candlekeeper.core.R
import id.candlekeeper.core.databinding.ItemAdNativeStartioBigBinding
import id.candlekeeper.core.databinding.ItemAdNativeStartioSmallBinding
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.core.utils.hide
import id.candlekeeper.core.utils.show
import id.candlekeeper.core.utils.toast


fun getAdsInPref(prefManager: PrefManager, idName: String): Admob {
    var resultAdmob = Admob()

    prefManager.spGetAdsAdmob()?.map {
        if (it.name == idName) {
            resultAdmob = it
        }
    }
    return resultAdmob
}

/*----------------------------------GET NATIVE ADS--------------------------*/
fun getIdNativeHeroList(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "native_hero_list")
}

fun getIdNativeSkinsList(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "native_skins_list")
}

fun getIdNativeDetailList(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "native_detail_list")
}

fun getIdNativeDialog(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "native_dialog")
}

fun getIdNativeCarousel(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "native_carousel")
}


/*----------------------------------OPERATION NATIVE ADS--------------------------*/
private fun inputAdToList(
    sizeItem: Int,
    item: MutableList<Any>,
    itemAd: MutableList<NativeAd>,
    from: String,
    startIndex: Int
) {
    Log.e("itemAd", "size $from :" + itemAd.size.toString())

    if (sizeItem >= 4) {
        val offset = sizeItem / (itemAd.size)
        var index = startIndex
        for (ad in itemAd) {
            item.add(index, ad)
            index = index.plus(offset)
        }
    } else {
        val offset: Int = sizeItem / itemAd.size + 1
        var index = startIndex
        for (ad in itemAd) {
            item.add(index, ad)
            index += offset
        }
    }
}

private fun inputAdToListStartIo(
    sizeItem: Int,
    item: MutableList<Any>,
    itemAd: MutableList<NativeAdDetails>,
    from: String,
    startIndex: Int
) {
    Log.e("itemAd", "size $from :" + itemAd.size.toString())

    if (sizeItem >= 4) {
        val offset = sizeItem / (itemAd.size)
        var index = startIndex
        for (ad in itemAd) {
            item.add(index, ad)
            index = index.plus(offset)
        }
    } else {
        val offset: Int = sizeItem / itemAd.size + 1
        var index = startIndex
        for (ad in itemAd) {
            item.add(index, ad)
            index += offset
        }
    }
}

fun setMaxLoadAd(
    size: Int,
    process: String,
    nativeAdList: MutableList<NativeAd>?,
    nativeAd: NativeAd?
): Int {
    when {
        size <= 5 -> {
            if (process == "setAdToList") {
                if (nativeAdList?.size!! + 1 <= 1) {
                    nativeAdList.add(nativeAd!!)
                }
            }
            return 1
        }
        size in 6..11 -> {
            if (process == "setAdToList") {
                if (nativeAdList?.size!! + 1 <= 2) {
                    nativeAdList.add(nativeAd!!)
                }
            }
            return 2
        }
        size in 12..19 -> {
            if (process == "setAdToList") {
                if (nativeAdList?.size!! + 1 <= 3) {
                    nativeAdList.add(nativeAd!!)
                }
            }
            return 3
        }
        size in 20..29 -> {
            if (process == "setAdToList") {
                if (nativeAdList?.size!! + 1 <= 4) {
                    nativeAdList.add(nativeAd!!)
                }
            }
            return 4
        }
        size >= 30 -> {
            if (process == "setAdToList") {
                if (nativeAdList?.size!! + 1 <= 5) {
                    nativeAdList.add(nativeAd!!)
                }
            }
            return 5
        }
    }
    return 0
}


/*----------------------------------POPULATE NATIVE AD--------------------------*/
fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
    // Set the media view.
    adView.mediaView = adView.findViewById(R.id.ad_media)

    // Set other ad assets.
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)
    adView.priceView = adView.findViewById(R.id.ad_price)
    adView.starRatingView = adView.findViewById(R.id.ad_stars)
    adView.storeView = adView.findViewById(R.id.ad_store)
    adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

    // The headline and media content are guaranteed to be in every UnifiedNativeAd.
    (adView.headlineView as TextView).text = nativeAd.headline
    adView.mediaView.setMediaContent(nativeAd.mediaContent)

    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    // check before trying to display them.
    if (nativeAd.body == null) {
        adView.bodyView.visibility = View.INVISIBLE
    } else {
        adView.bodyView.visibility = View.VISIBLE
        (adView.bodyView as TextView).text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        adView.callToActionView.visibility = View.INVISIBLE
    } else {
        adView.callToActionView.visibility = View.VISIBLE
        (adView.callToActionView as Button).text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        adView.iconView.visibility = View.GONE
    } else {
        (adView.iconView as ImageView).setImageDrawable(
            nativeAd.icon.drawable
        )
        adView.iconView.visibility = View.VISIBLE
    }

    if (nativeAd.price == null) {
        adView.priceView.visibility = View.INVISIBLE
    } else {
        adView.priceView.visibility = View.VISIBLE
        (adView.priceView as TextView).text = nativeAd.price
    }

    if (nativeAd.store == null) {
        adView.storeView.visibility = View.INVISIBLE
    } else {
        adView.storeView.visibility = View.VISIBLE
        (adView.storeView as TextView).text = nativeAd.store
    }

    if (nativeAd.starRating == null) {
        adView.starRatingView.visibility = View.INVISIBLE
    } else {
        (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
        adView.starRatingView.visibility = View.VISIBLE
    }

    if (nativeAd.advertiser == null) {
        adView.advertiserView.visibility = View.INVISIBLE
    } else {
        (adView.advertiserView as TextView).text = nativeAd.advertiser
        adView.advertiserView.visibility = View.VISIBLE
    }

    // This method tells the Google Mobile Ads SDK that you have finished populating your
    // native ad view with this native ad.
    adView.setNativeAd(nativeAd)
}

/*----------------------------------POPULATE STARTIO AD--------------------------*/
fun populateNativeStartIoBig(data: NativeAdDetails, binding: ItemAdNativeStartioBigBinding) {
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

fun populateNativeStartIoSmall(data: NativeAdDetails, binding: ItemAdNativeStartioSmallBinding) {
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

/*----------------------------------POPULATE AD APPLOVIN--------------------------*/
fun populateNativeApplovin(activity: Activity): MaxNativeAdView {
    val binder: MaxNativeAdViewBinder =
        MaxNativeAdViewBinder.Builder(R.layout.item_ad_native_applovin)
            .setTitleTextViewId(R.id.title_text_view)
            .setBodyTextViewId(R.id.body_text_view)
            .setAdvertiserTextViewId(R.id.advertiser_textView)
            .setIconImageViewId(R.id.icon_image_view)
            .setMediaContentViewGroupId(R.id.media_view_container)
            .setOptionsContentViewGroupId(R.id.options_view)
            .setCallToActionButtonId(R.id.cta_button)
            .build()
    return MaxNativeAdView(binder, activity)
}

fun populateNativeCarouselApplovin(activity: Activity): MaxNativeAdView {
    val binder: MaxNativeAdViewBinder =
        MaxNativeAdViewBinder.Builder(R.layout.item_ad_native_applovin_carousel)
            .setTitleTextViewId(R.id.ad_headline)
            .setBodyTextViewId(R.id.ad_body)
            .setAdvertiserTextViewId(R.id.ad_advertiser)
            .setIconImageViewId(R.id.ad_app_icon)
            .setMediaContentViewGroupId(R.id.ad_media)
            .setOptionsContentViewGroupId(R.id.linearLayout3)
            .setCallToActionButtonId(R.id.ad_call_to_action)
            .build()
    return MaxNativeAdView(binder, activity)
}


/*---------------------------NATIVE IN RECYCLER--------------------------*/
fun setupAdInRecycler(
    size: Int,
    dataNativeAds: Admob,
    nativeAdList: MutableList<NativeAd>?,
    context: Context,
    dataList: MutableList<Any>,
    adRequest: AdRequest,
    from: String,
    startIndex: Int,
    activity: Activity,
    onSubmit: () -> Unit
) {

    nativeAdList?.clear()
    var adLoader: AdLoader? = null
    val maxLoadAd = setMaxLoadAd(size, "", null, null)

    val builder = AdLoader.Builder(context, dataNativeAds.adsIdAdmob!!)
    adLoader = builder.forNativeAd { nativeAd ->
        setMaxLoadAd(size, "setAdToList", nativeAdList, nativeAd)

        if (!adLoader!!.isLoading) {
            try {
                inputAdToList(size, dataList, nativeAdList!!, from, startIndex)
                onSubmit.invoke()
            } catch (e: Exception) {
            }
        }

    }.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            Log.e("ads native $from", adError.message)
            setupAdAppLovinInRecycler(
                activity,
                context,
                dataNativeAds,
                dataList,
                startIndex,
                from
            ) {
                onSubmit.invoke()
            }
        }
    }
    ).build()

    if (dataNativeAds.isMultipleLoad!!) {
        adLoader!!.loadAds(adRequest, maxLoadAd)
    } else {
        adLoader!!.loadAd(adRequest)
    }
}

fun setupAdAppLovinInRecycler(
    activity: Activity,
    context: Context,
    dataNativeAds: Admob,
    dataList: MutableList<Any>,
    startIndex: Int,
    from: String? = "",
    onSubmit: () -> Unit
) {

    var nativeAd: MaxAd? = null
    val nativeAdLoader = MaxNativeAdLoader(dataNativeAds.adsIdApplovin, activity)
    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
            if (nativeAd != null) {
                nativeAdLoader.destroy(nativeAd)
            }
            nativeAd = ad
            dataList.add(startIndex, nativeAdView!!)
            onSubmit.invoke()

            if (dataNativeAds.isMultipleLoad!!) {
                setupAdStartIoInRecycler(
                    dataList.size, dataNativeAds, context, dataList, from!!, startIndex + 3, 4
                ) {
                    onSubmit.invoke()
                }
            }
        }

        override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
            Log.e("ads native $from", error.toString())
            setupAdStartIoInRecycler(
                dataList.size, dataNativeAds, context, dataList, from!!, startIndex, 4
            ) {
                onSubmit.invoke()
            }
        }
    })

    if (from == "carousel") {
        nativeAdLoader.loadAd(populateNativeCarouselApplovin(activity))
    } else {
        nativeAdLoader.loadAd(populateNativeApplovin(activity))
    }
}

fun setupAdStartIoInRecycler(
    size: Int,
    dataNativeAds: Admob,
    context: Context,
    dataList: MutableList<Any>,
    from: String,
    startIndex: Int,
    sizeImageAsset: Int,
    onSubmit: () -> Unit
) {

    val nativeAd = StartAppNativeAd(context)
    val maxLoadAd = if (dataNativeAds.isMultipleLoad!!) setMaxLoadAd(size, "", null, null) else 1
    nativeAd.loadAd(
        NativeAdPreferences()
            .setAdsNumber(maxLoadAd)
            .setAutoBitmapDownload(true)
            .setPrimaryImageSize(sizeImageAsset), object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
                inputAdToListStartIo(size, dataList, nativeAd.nativeAds, from, startIndex)
                onSubmit.invoke()
            }

            override fun onFailedToReceiveAd(ad: Ad?) {
                Log.e("ads native $from", ad!!.errorMessage!!)
            }
        }
    )
}

/*-------------------------NATIVE BIG IN ACT OR FRAGMENT--------------------------*/
fun setupAdNativeBig(
    dataNativeAds: Admob,
    context: Context,
    activity: Activity,
    fragmentActivity: FragmentActivity,
    dialogFragment: DialogFragment,
    nativeAdView: NativeAdView,
    frameLayout: FrameLayout,
    binding: ItemAdNativeStartioBigBinding,
    adRequest: AdRequest,
    lottieAnimationView: LottieAnimationView?,
    button: AppCompatButton?
) {

    val builder = AdLoader.Builder(context, dataNativeAds.adsIdAdmob!!)
        .forNativeAd { nativeAd ->

            if (fragmentActivity.isFinishing || fragmentActivity.isChangingConfigurations || dialogFragment.isRemoving || dialogFragment.activity == null || dialogFragment.isDetached || !dialogFragment.isAdded || dialogFragment.view == null) {
                nativeAd.destroy()
            } else {
                populateNativeAdView(nativeAd, nativeAdView)
                with(frameLayout) {
                    removeAllViews()
                    addView(nativeAdView)
                }
            }
        }
        .withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                lottieAnimationView?.hide()
                button?.show()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("ads_native_big", adError.toString())
                setupAdNativeAppLovinBig(
                    dataNativeAds,
                    context,
                    fragmentActivity,
                    dialogFragment,
                    frameLayout,
                    activity,
                    lottieAnimationView,
                    button,
                    binding
                )
            }
        }).build()
    builder.loadAd(adRequest)
}

fun setupAdNativeAppLovinBig(
    dataNativeAds: Admob,
    context: Context,
    fragmentActivity: FragmentActivity,
    dialogFragment: DialogFragment,
    frameLayout: FrameLayout,
    activity: Activity,
    lottieAnimationView: LottieAnimationView?,
    button: AppCompatButton?,
    binding: ItemAdNativeStartioBigBinding
) {

    var nativeAd: MaxAd? = null
    val nativeAdLoader = MaxNativeAdLoader(dataNativeAds.adsIdApplovin, fragmentActivity)
    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
            if (nativeAd != null) {
                nativeAdLoader.destroy(nativeAd)
            }
            nativeAd = ad

            if (fragmentActivity.isFinishing || fragmentActivity.isChangingConfigurations || dialogFragment.isRemoving || dialogFragment.activity == null || dialogFragment.isDetached || !dialogFragment.isAdded || dialogFragment.view == null) {
                nativeAdLoader.destroy(ad)
            } else {
                with(frameLayout) {
                    removeAllViews()
                    addView(nativeAdView)
                    lottieAnimationView?.hide()
                    button?.show()
                }
            }
        }

        override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
            Log.e("ads_native_big", error.toString())
            setupAdNativeStartIoBig(context, 4, binding, frameLayout, lottieAnimationView, button)
        }
    })
    nativeAdLoader.loadAd(populateNativeApplovin(activity))
}

fun setupAdNativeStartIoBig(
    context: Context,
    sizeImageAsset: Int,
    binding: ItemAdNativeStartioBigBinding,
    frameLayout: FrameLayout,
    lottieAnimationView: LottieAnimationView?,
    button: AppCompatButton?
) {

    val nativeAd = StartAppNativeAd(context)
    nativeAd.loadAd(
        NativeAdPreferences()
            .setAdsNumber(1)
            .setAutoBitmapDownload(true)
            .setPrimaryImageSize(sizeImageAsset), object : AdEventListener {
            override fun onReceiveAd(p0: Ad) {
                populateNativeStartIoBig(nativeAd.nativeAds.first(), binding)
                with(frameLayout) {
                    removeAllViews()
                    addView(binding.root)
                }
                lottieAnimationView?.hide()
                button?.show()
            }

            override fun onFailedToReceiveAd(p0: Ad?) {
                Log.e("ads_native_big", p0!!.errorMessage!!)
                button?.show()
            }
        }
    )

}


/*-------------------------NATIVE SMALL IN ACT OR FRAGMENT--------------------------*/
fun setupAdsNativeApplovinSmall(
    context: Context,
    binding: ItemAdNativeStartioSmallBinding,
    dataNativeAds: Admob,
    fragmentActivity: FragmentActivity,
    dialogFragment: DialogFragment? = null,
    frameLayout: FrameLayout
) {

    var nativeAd: MaxAd? = null
    val nativeAdLoader = MaxNativeAdLoader(dataNativeAds.adsIdApplovinSmall, fragmentActivity)
    nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

        override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
            if (nativeAd != null) {
                nativeAdLoader.destroy(nativeAd)
            }
            nativeAd = ad

            if (fragmentActivity.isFinishing || fragmentActivity.isChangingConfigurations || dialogFragment!!.isRemoving || dialogFragment.activity == null || dialogFragment.isDetached || !dialogFragment.isAdded || dialogFragment.view == null) {
                nativeAdLoader.destroy(ad)
            } else {
                with(frameLayout) {
                    removeAllViews()
                    addView(nativeAdView)
                }
            }
        }

        override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
            Log.e("ads_native_small", error.toString())
            setupAdsNativeStartIoSmall(context, binding, frameLayout)
        }
    })
    nativeAdLoader.loadAd()
}

fun setupAdsNativeStartIoSmall(
    context: Context,
    binding: ItemAdNativeStartioSmallBinding,
    frameLayout: FrameLayout
) {

    val nativeAd = StartAppNativeAd(context)
    nativeAd.loadAd(
        NativeAdPreferences()
            .setAdsNumber(1)
            .setAutoBitmapDownload(true)
            .setPrimaryImageSize(3), object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
                populateNativeStartIoSmall(nativeAd.nativeAds.first(), binding)
                with(frameLayout) {
                    removeAllViews()
                    addView(binding.root)
                }
            }

            override fun onFailedToReceiveAd(p0: Ad?) {
                Log.e("native_startio", p0!!.errorMessage!!)
            }
        }
    )
}