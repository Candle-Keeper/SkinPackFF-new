package id.candlekeeper.core.domain.model.dataBaseApp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppStatus(
    var id: Int? = 0,
    var version: String? = "",
    var version_code: Int? = 0,
    var isServerDown: Boolean? = false
) : Parcelable