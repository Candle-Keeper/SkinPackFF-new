package id.candlekeeper.core.domain.model.dataAdditional

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UrlTutorial(
    var id: Int? = 0,
    var name: String = "",
    var youtubeUrl: String? = ""
) : Parcelable