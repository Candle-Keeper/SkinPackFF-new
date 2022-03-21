package id.candlekeeper.core.domain.model.dataIncome

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Admob(
    var id: Int? = 0,
    var name: String? = "",
    var isEnable: Boolean? = false,
    var isMultipleLoad: Boolean? = false,
    var adsIdAdmob: String? = "",
    var adsIdApplovin: String? = "",
    var adsIdApplovinSmall: String? = "",
    var adsIdStartio: String? = "",
    var adsIdStartioSmall: String? = "",
    var countLoop: Int? = 0,
    var typeAdsBanner: String? = "",
) : Parcelable