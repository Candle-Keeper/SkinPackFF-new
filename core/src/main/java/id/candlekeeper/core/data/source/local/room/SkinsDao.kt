package id.candlekeeper.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.candlekeeper.core.data.source.local.entity.dataContent.*
import id.candlekeeper.core.data.source.local.entity.dataIncome.EndorseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkinsDao {

    //content feature

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeroes(insert: List<HeroesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkins(insert: List<SkinsEntity>)

    @Query("SELECT * FROM heroesEntities WHERE idCategory =:idCategory ORDER BY name ASC")
    fun getHeroes(idCategory: Int): Flow<List<HeroesEntity>>

    @Query("SELECT * FROM skinsEntities WHERE idHeroes =:idHero ORDER BY name ASC")
    fun getSkins(idHero: Int): Flow<List<SkinsEntity>>

    @Query("SELECT * FROM heroesEntities WHERE idCategory =:idCategory AND name LIKE '%' || :search || '%'")
    fun searchHeroes(idCategory: Int, search: String): Flow<List<HeroesEntity>>

    @Query("SELECT * FROM skinsEntities WHERE idHeroes =:idHero AND name LIKE '%' || :search || '%'")
    fun searchSkins(idHero: Int, search: String): Flow<List<SkinsEntity>>


    //endorse feature
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEndorse(insert: List<EndorseEntity>)

    @Query("SELECT * FROM endorseEntities ORDER BY name ASC")
    fun getEndorse(): Flow<List<EndorseEntity>>
}