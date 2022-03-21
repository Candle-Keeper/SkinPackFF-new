package id.candlekeeper.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListDataAdditionalResponse(
    @field:SerializedName("data")
    val data: List<DataAdditionalResponse>
)

data class DataAdditionalResponse(
    //url tutorial
    @field:SerializedName("id_url_tutorial")
    var id_url_tutorial: Int? = 0,

    @field:SerializedName("name")
    var name: String? = "",

    @field:SerializedName("youtube_url")
    var youtube_url: String? = ""
)