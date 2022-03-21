package id.candlekeeper.core.data.source.local.entity.dataContent

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoryEntities")
data class CategoryEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idCategory")
    var idCategory: Int? = 0,

    @ColumnInfo(name = "name")
    var name: String? = "",

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = ""
)