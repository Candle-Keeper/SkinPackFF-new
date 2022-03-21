package id.candlekeeper.core.domain.model.dataIncome

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Endorse(
    var id: Int? = 0,
    var name: String? = "",
    var title: String? = "",
    var isEnable: Boolean? = false,
    var isMultipleLoad: Boolean? = false,
    var description: String? = "",
    var showingIndex: Int? = 0,
    var packageApp: String? = "",
    var imageUrl: String? = "",
    var youtubeUrl: String? = "",
    var activityUrl: String? = "",
    var webUrl: String? = ""
) : Parcelable