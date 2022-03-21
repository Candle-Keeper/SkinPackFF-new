package id.candlekeeper.core.domain.model.dataContent

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Heroes(
    var idHeroes: Int? = 0,
    var idCategory: Int? = 0,
    var name: String = "",
    var image_url: String? = "",
    var isAdditional: Boolean? = false
) : Parcelable