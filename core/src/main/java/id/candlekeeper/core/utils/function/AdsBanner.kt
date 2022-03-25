package id.candlekeeper.core.utils.function

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.core.utils.adsBannerType

fun getIdBannerMore(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "banner_more")
}

fun getIdBannerDialog(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "banner_dialog")
}

fun getIdBannerDetail(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "banner_detail")
}

fun getIdBannerDownload(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "banner_download")
}

//fun showAdsBanner(
//    context: Context,
//    activity: Activity,
//    bannerData: Admob,
//    view: ViewGroup,
//    adRequest: AdRequest
//) {
//    val mAdView = AdView(context)
//    mAdView.adSize = adsBannerType(bannerData, activity, context)
//    mAdView.adUnitId = bannerData.adsIdAdmob
//    view.addView(mAdView)
//    mAdView.loadAd(adRequest)
//}