package id.candlekeeper.core.data.source.local.entity.dataContent

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addHeroesEntities")
data class AddHeroesEntity(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idHeroes")
    var idHeroes: Int? = 0,

    @ColumnInfo(name = "idCategory")
    var idCategory: Int? = 0,

    @ColumnInfo(name = "name")
    var name: String? = "",

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = ""
)