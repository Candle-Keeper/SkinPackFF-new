package id.candlekeeper.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.candlekeeper.core.data.source.local.entity.dataContent.*
import id.candlekeeper.core.data.source.local.entity.dataIncome.EndorseEntity

@Database(
    entities = [
        HeroesEntity::class,
        SkinsEntity::class,
        EndorseEntity::class], version = 10, exportSchema = false
)

abstract class SkinsDatabase : RoomDatabase() {

    abstract fun skinsDao(): SkinsDao

}