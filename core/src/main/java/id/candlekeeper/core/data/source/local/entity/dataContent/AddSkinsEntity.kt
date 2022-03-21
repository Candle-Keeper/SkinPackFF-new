package id.candlekeeper.core.data.source.local.entity.dataContent

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "addSkinsEntities")
data class AddSkinsEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idScript")
    var idScript: Int? = 0,

    @ColumnInfo(name = "idHeroes")
    var idHeroes: Int? = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = "",

    @ColumnInfo(name = "release")
    var release: String? = "",

    @ColumnInfo(name = "size")
    var size: String? = "",

    @ColumnInfo(name = "fileUrl")
    var fileUrl: String? = "",

    @ColumnInfo(name = "youtubeUrl")
    var youtubeUrl: String? = ""
)