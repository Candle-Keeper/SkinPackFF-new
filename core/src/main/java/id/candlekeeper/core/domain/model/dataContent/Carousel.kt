package id.candlekeeper.core.domain.model.dataContent

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Carousel(
    var idCarousel: Int? = 0,
    var name: String = "",
    var imageUrl: String? = "",
    var linkUrl: String? = "",
    var activityUrl: String? = ""
) : Parcelable