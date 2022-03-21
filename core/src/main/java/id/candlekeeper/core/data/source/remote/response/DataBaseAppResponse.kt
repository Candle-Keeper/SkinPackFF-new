package id.candlekeeper.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListDataBaseAppResponse(
    @field:SerializedName("data")
    val data: List<DataBaseAppResponse>
)

data class DataBaseAppResponse(

    //App Status
    @field:SerializedName("id_app_status")
    var id_app_status: Int? = 0,

    @field:SerializedName("type")
    var type: String? = "",

    @field:SerializedName("version")
    var version: String? = "",

    @field:SerializedName("version_code")
    var version_code: Int? = 0,

    @field:SerializedName("is_server_down")
    var is_server_down: Boolean? = false,


    //request method
    @field:SerializedName("id_request_method")
    var id_request_method: Int? = 0,

    @field:SerializedName("is_online_request")
    var is_online_request: Boolean? = false,

    @field:SerializedName("name")
    var name: String? = "",


    //dynamic token
    @field:SerializedName("token")
    var token: String? = "",

    @field:SerializedName("base_file_url")
    var base_files: String? = ""
)