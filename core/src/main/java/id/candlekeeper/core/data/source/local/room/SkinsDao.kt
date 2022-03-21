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
    suspend fun insertCategory(insert: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeroes(insert: List<HeroesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkins(insert: List<SkinsEntity>)

    @Query("SELECT * FROM categoryEntities ORDER BY name ASC")
    fun getCategory(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM heroesEntities WHERE idCategory =:idCategory ORDER BY name ASC")
    fun getHeroes(idCategory: Int): Flow<List<HeroesEntity>>

    @Query("SELECT * FROM skinsEntities WHERE idHeroes =:idHero ORDER BY name ASC")
    fun getSkins(idHero: Int): Flow<List<SkinsEntity>>

    @Query("SELECT * FROM heroesEntities WHERE idCategory =:idCategory AND name LIKE '%' || :search || '%'")
    fun searchHeroes(idCategory: Int, search: String): Flow<List<HeroesEntity>>

    @Query("SELECT * FROM skinsEntities WHERE idHeroes =:idHero AND name LIKE '%' || :search || '%'")
    fun searchSkins(idHero: Int, search: String): Flow<List<SkinsEntity>>


    //additonal content feature
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddCategory(insert: List<AddCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddHeroes(insert: List<AddHeroesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddSkins(insert: List<AddSkinsEntity>)

    @Query("SELECT * FROM addCategoryEntities ORDER BY name ASC")
    fun getAddCategory(): Flow<List<AddCategoryEntity>>

    @Query("SELECT * FROM addHeroesEntities WHERE idCategory =:idCategory ORDER BY name ASC")
    fun getAddHeroes(idCategory: Int): Flow<List<AddHeroesEntity>>

    @Query("SELECT * FROM addSkinsEntities WHERE idHeroes =:idHero ORDER BY name ASC")
    fun getAddSkins(idHero: Int): Flow<List<AddSkinsEntity>>

    @Query("SELECT * FROM addSkinsEntities ORDER BY name ASC")
    fun getAllAddSkins(): Flow<List<AddSkinsEntity>>

    @Query("SELECT * FROM addHeroesEntities WHERE idCategory =:idCategory AND name LIKE '%' || :search || '%'")
    fun searchAddHeroes(idCategory: Int, search: String): Flow<List<AddHeroesEntity>>

    @Query("SELECT * FROM addSkinsEntities WHERE idHeroes =:idHero AND name LIKE '%' || :search || '%'")
    fun searchAddSkins(idHero: Int, search: String): Flow<List<AddSkinsEntity>>


    //endorse feature
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEndorse(insert: List<EndorseEntity>)

    @Query("SELECT * FROM endorseEntities ORDER BY name ASC")
    fun getEndorse(): Flow<List<EndorseEntity>>
}