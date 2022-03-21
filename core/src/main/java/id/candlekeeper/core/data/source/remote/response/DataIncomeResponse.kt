package id.candlekeeper.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListDataIncomeResponse(
    @field:SerializedName("data")
    val data: List<DataIncomeResponse>
)

data class DataIncomeResponse(
    //Admob Response
    @field:SerializedName("id_admob")
    var id_admob: Int? = 0,

    @field:SerializedName("type")
    var type: String? = "",

    @field:SerializedName("name")
    var name: String? = "",

    @field:SerializedName("is_enable")
    var is_enable: Boolean? = false,

    @field:SerializedName("is_multiple_load")
    var is_multiple_load: Boolean? = false,

    @field:SerializedName("ads_id_admob")
    var ads_id_admob: String? = "",

    @field:SerializedName("ads_id_applovin")
    var ads_id_applovin: String? = "",

    @field:SerializedName("ads_id_applovin_small")
    var ads_id_applovin_small: String? = "",

    @field:SerializedName("ads_id_startio")
    var ads_id_startio: String? = "",

    @field:SerializedName("ads_id_startio_small")
    var ads_id_startio_small: String? = "",

    @field:SerializedName("count_loop")
    var count_loop: Int? = 0,

    @field:SerializedName("type_ads_banner")
    var type_ads_banner: String? = "",


    //Endorse Response
    @field:SerializedName("id_endorse")
    var id_endorse: Int? = 0,

    @field:SerializedName("title")
    var title: String? = "",

    @field:SerializedName("description")
    var description: String? = "",

    @field:SerializedName("showing_index")
    var showing_index: Int? = 0,

    @field:SerializedName("package_app")
    var package_app: String? = "",

    @field:SerializedName("image_url")
    var image_url: String? = "",

    @field:SerializedName("youtube_url")
    var youtube_url: String? = "",

    @field:SerializedName("activity_url")
    var activity_url: String? = "",

    @field:SerializedName("web_url")
    var web_url: String? = ""
)