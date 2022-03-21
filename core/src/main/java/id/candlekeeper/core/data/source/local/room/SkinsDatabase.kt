package id.candlekeeper.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.candlekeeper.core.data.source.local.entity.dataContent.*
import id.candlekeeper.core.data.source.local.entity.dataIncome.EndorseEntity

@Database(
    entities = [
        CategoryEntity::class,
        HeroesEntity::class,
        SkinsEntity::class,
        EndorseEntity::class,
        AddCategoryEntity::class,
        AddHeroesEntity::class,
        AddSkinsEntity::class], version = 9, exportSchema = false
)

abstract class SkinsDatabase : RoomDatabase() {

    abstract fun skinsDao(): SkinsDao

}