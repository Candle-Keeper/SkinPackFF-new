package id.candlekeeper.core.utils.function

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.startapp.sdk.adsbase.StartAppAd
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.utils.PrefManager


var mInterstitialAdmob: InterstitialAd? = null
private var mInterstisialMax: MaxInterstitialAd? = null
private var isFailAdsApplovin = false


/*----------------------------------LOAD INTERSTISIAL--------------------------*/
private fun loadAdsAdmob(
    context: Context,
    activity: Activity,
    adRequest: AdRequest,
    dataAdsInters: Admob
) {
    InterstitialAd.load(
        context,
        dataAdsInters.adsIdAdmob!!,
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("loadAdsAdmob", adError.message)
                mInterstitialAdmob = null
                loadAdsIntersAppLovin(activity, dataAdsInters)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.e("onAdLoaded", interstitialAd.toString())
                mInterstitialAdmob = interstitialAd
            }
        })
}

private fun loadAdsIntersAppLovin(activity: Activity, dataAdsInters: Admob) {
    mInterstisialMax = MaxInterstitialAd(dataAdsInters.adsIdApplovin, activity)
    mInterstisialMax!!.loadAd()
    mInterstisialMax!!.setListener(object : MaxAdListener {
        override fun onAdLoaded(ad: MaxAd?) {
            Log.e("onAdLoaded", ad.toString())
        }

        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
            Log.e("loadAdsIntersAppLovin", error!!.message)
            mInterstisialMax = null
            isFailAdsApplovin = true
        }

        override fun onAdDisplayed(ad: MaxAd?) {
            Log.e("onAdDisplayed", ad.toString())
        }

        override fun onAdHidden(ad: MaxAd?) {
            Log.e("onAdHidden", ad.toString())
            mInterstisialMax!!.loadAd()
        }

        override fun onAdClicked(ad: MaxAd?) {}
        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
            Log.e("onAdDisplayFailed", error!!.message)
            mInterstisialMax!!.loadAd()
        }
    })
}

fun loadAdInterstisial(
    context: Context,
    activity: Activity,
    adRequest: AdRequest,
    prefManager: PrefManager,
    idName: String
): Admob {
    val dataAdsInters = getAdsInPref(prefManager, idName)
    if (dataAdsInters.isEnable!!) {
        if (mInterstitialAdmob == null) {
            loadAdsAdmob(context, activity, adRequest, dataAdsInters)
        }
    }
    return dataAdsInters
}

fun loadAdsActivityIntertisial(
    context: Context,
    activity: Activity,
    adRequest: AdRequest,
    prefManager: PrefManager
) {
    loadAdInterstisial(context, activity, adRequest, prefManager, "interstisial_activity")
}

fun loadAdsAllDialogIntertisial(
    context: Context,
    activity: Activity,
    adRequest: AdRequest,
    prefManager: PrefManager
) {
    loadAdInterstisial(context, activity, adRequest, prefManager, "interstisial_all_dialog")
}

fun isNotLoadAdsInters(): Boolean {
    if (mInterstitialAdmob == null && mInterstisialMax == null)
        return true
    return false
}


/*----------------------------------SHOW INTERSTISIAL--------------------------*/
fun showAdsHeroListIntertisial(
    context: Context,
    activity: Activity,
    prefManager: PrefManager
) {
    val dataAdsIntersHero = getAdsInPref(prefManager, "interstisial_hero_list")
    if (dataAdsIntersHero.isEnable!!) {
        if (mInterstitialAdmob != null) {
            showAdsIntersAdmob(activity)
        } else if (mInterstisialMax != null) {
            showAdsIntersApplovin()
        } else if (isFailAdsApplovin) {
            showAdsIntersStartIo(context)
        }
    }
}

fun showAdsSKinsListIntertisial(
    context: Context,
    activity: Activity,
    prefManager: PrefManager
) {
    val dataAdsIntersSkins = getAdsInPref(prefManager, "interstisial_skins_list")
    if (dataAdsIntersSkins.isEnable!!) {
        if (mInterstitialAdmob != null) {
            showAdsIntersAdmob(activity)
        } else if (mInterstisialMax != null) {
            showAdsIntersApplovin()
        } else if (isFailAdsApplovin) {
            showAdsIntersStartIo(context)
        }
    }
}

fun showAdsDetailIntertisial(
    context: Context,
    activity: Activity,
    prefManager: PrefManager
) {
    val dataAdsIntersDetail = getAdsInPref(prefManager, "interstisial_detail")
    if (dataAdsIntersDetail.isEnable!!) {
        if (mInterstitialAdmob != null) {
            showAdsIntersAdmob(activity)
        } else if (mInterstisialMax != null) {
            showAdsIntersApplovin()
        } else if (isFailAdsApplovin) {
            showAdsIntersStartIo(context)
        }
    }
}

fun showAdsAllDialogIntertisial(
    context: Context,
    activity: Activity,
    prefManager: PrefManager
) {
    val dataAdsIntersDialog = getAdsInPref(prefManager, "interstisial_all_dialog")
    if (dataAdsIntersDialog.isEnable!!) {
        if (mInterstitialAdmob != null) {
            showAdsIntersAdmob(activity)
        } else if (mInterstisialMax != null) {
            showAdsIntersApplovin()
        } else if (isFailAdsApplovin) {
            showAdsIntersStartIo(context)
        }
    }
}


/*----------------------------------INTERSTISIAL OPERATION--------------------------*/
fun showAdsIntersAdmob(activity: Activity) {
    if (mInterstitialAdmob != null) {
        mInterstitialAdmob?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                mInterstitialAdmob = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                Log.e("onAdFailedToShowFull", p0.message)
            }
        }

        mInterstitialAdmob?.show(activity)
    } else {
        Log.e("showAdsIntersAdmob", "ads inters not show")
    }
}

fun showAdsIntersApplovin() {
    if (mInterstisialMax!!.isReady) {
        mInterstisialMax!!.showAd()
        mInterstisialMax = null
    }
}

fun showAdsIntersStartIo(context: Context) {
    StartAppAd.AdMode.AUTOMATIC
    StartAppAd.showAd(context)
    isFailAdsApplovin = false
}