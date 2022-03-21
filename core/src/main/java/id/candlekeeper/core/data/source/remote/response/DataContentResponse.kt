package id.candlekeeper.core.data.source.remote.response

import com.google.gson.annotations.SerializedName


data class ListDataContentResponse(
    @field:SerializedName("data")
    val data: List<DataContentResponse>
)

data class DataContentResponse(

    //new
    @field:SerializedName("id_category")
    var id_category: Int? = 0,

    @field:SerializedName("id_heroes")
    var id_heroes: Int? = 0,

    @field:SerializedName("id_script")
    var id_script: Int? = 0,

    @field:SerializedName("id_content")
    var id_content: Int? = 0,

    @field:SerializedName("name")
    var name: String? = "",

    @field:SerializedName("image_url")
    var image_url: String? = "",

    @field:SerializedName("file_url")
    var file_url: String? = "",

    @field:SerializedName("youtube_url")
    var youtube_url: String? = "",

    @field:SerializedName("release")
    var release: String? = "",

    @field:SerializedName("size")
    var size: String? = "",

    @field:SerializedName("link_url")
    var link_url: String? = "",

    @field:SerializedName("activity_url")
    var activity_url: String? = "",

    @field:SerializedName("is_hide")
    var is_hide: Boolean? = false,
)