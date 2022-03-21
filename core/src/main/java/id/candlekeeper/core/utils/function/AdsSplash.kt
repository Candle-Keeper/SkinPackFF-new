package id.candlekeeper.core.utils.function

import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.utils.PrefManager

fun getIdSplashAd(prefManager: PrefManager): Admob {
    return getAdsInPref(prefManager, "splash_ad")
}