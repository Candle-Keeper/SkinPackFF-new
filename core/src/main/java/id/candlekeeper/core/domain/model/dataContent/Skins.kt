package id.candlekeeper.core.domain.model.dataContent

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Skins(
    var idScript: Int? = 0,
    var idHero: Int? = 0,
    var name: String = "",
    var imageUrl: String? = "",
    var release: String? = "",
    var size: String? = "",
    var fileUrl: String? = "",
    var youtubeUrl: String? = "",
    var isHide: Boolean? = false,
    var isMax: Boolean? = false
) : Parcelable