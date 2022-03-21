package id.candlekeeper.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListDataMonitorResponse(
    @field:SerializedName("data")
    val data: List<DataMonitorResponse>
)

data class DataMonitorResponse(
    //Request Skins Response
    var id_monitoring: Int? = 0,
    var type: String? = "",
    var device: String = "",
    var script_url: String? = "",
    var script_name: String? = "",
    var message: String? = ""
)