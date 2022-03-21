package id.candlekeeper.core.data.source.local.entity.dataIncome

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "endorseEntities")
data class EndorseEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int? = 0,

    @ColumnInfo(name = "name")
    var name: String? = "",

    @ColumnInfo(name = "title")
    var title: String? = "",

    @ColumnInfo(name = "isEnable")
    var isEnable: Boolean? = false,

    @ColumnInfo(name = "isMultipleLoad")
    var isMultipleLoad: Boolean? = false,

    @ColumnInfo(name = "description")
    var description: String? = "",

    @ColumnInfo(name = "showingIndex")
    var showingIndex: Int? = 0,

    @ColumnInfo(name = "packageApp")
    var packageApp: String? = "",

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = "",

    @ColumnInfo(name = "youtubeUrl")
    var youtubeUrl: String? = "",

    @ColumnInfo(name = "activityUrl")
    var activityUrl: String? = "",

    @ColumnInfo(name = "webUrl")
    var webUrl: String? = ""
)