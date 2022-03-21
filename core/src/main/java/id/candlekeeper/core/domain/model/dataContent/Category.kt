package id.candlekeeper.core.domain.model.dataContent

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var idCategory: Int? = 0,
    var name: String = "",
    var imageUrl: String? = "",
    var isAdditional: Boolean? = false
) : Parcelable