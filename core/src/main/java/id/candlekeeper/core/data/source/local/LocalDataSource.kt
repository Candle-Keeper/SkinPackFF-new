package id.candlekeeper.core.data.source.local

import id.candlekeeper.core.data.source.local.entity.dataContent.*
import id.candlekeeper.core.data.source.local.entity.dataIncome.EndorseEntity
import id.candlekeeper.core.data.source.local.room.SkinsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class LocalDataSource(private val mSkinsDao: SkinsDao) {

    //content feature
    suspend fun insertCategory(insert: List<CategoryEntity>) = mSkinsDao.insertCategory(insert)

    suspend fun insertHeroes(insert: List<HeroesEntity>) = mSkinsDao.insertHeroes(insert)

    suspend fun insertSkins(insert: List<SkinsEntity>) = mSkinsDao.insertSkins(insert)

    fun getCategory(): Flow<List<CategoryEntity>> = mSkinsDao.getCategory()

    fun getHeroes(idCategory: Int? = 0): Flow<List<HeroesEntity>> =
        mSkinsDao.getHeroes(idCategory!!)

    fun getSkins(idHero: Int? = 0): Flow<List<SkinsEntity>> = mSkinsDao.getSkins(idHero!!)

    fun searchHeroes(idCategory: Int, search: String): Flow<List<HeroesEntity>> =
        mSkinsDao.searchHeroes(idCategory, search).flowOn(Dispatchers.Default).conflate()

    fun searchSkins(idHero: Int, search: String): Flow<List<SkinsEntity>> =
        mSkinsDao.searchSkins(idHero, search).flowOn(Dispatchers.Default).conflate()


    //content feature
    suspend fun insertAddCategory(insert: List<AddCategoryEntity>) =
        mSkinsDao.insertAddCategory(insert)

    suspend fun insertAddHeroes(insert: List<AddHeroesEntity>) = mSkinsDao.insertAddHeroes(insert)

    suspend fun insertAddSkins(insert: List<AddSkinsEntity>) = mSkinsDao.insertAddSkins(insert)

    fun getAddCategory(): Flow<List<AddCategoryEntity>> = mSkinsDao.getAddCategory()

    fun getAddHeroes(idCategory: Int? = 0): Flow<List<AddHeroesEntity>> =
        mSkinsDao.getAddHeroes(idCategory!!)

    fun getAddSkins(idHero: Int? = 0): Flow<List<AddSkinsEntity>> = mSkinsDao.getAddSkins(idHero!!)

    fun getAllAddSkins(): Flow<List<AddSkinsEntity>> = mSkinsDao.getAllAddSkins()

    fun searchAddHeroes(idCategory: Int, search: String): Flow<List<AddHeroesEntity>> =
        mSkinsDao.searchAddHeroes(idCategory, search).flowOn(Dispatchers.Default).conflate()

    fun searchAddSkins(idHero: Int, search: String): Flow<List<AddSkinsEntity>> =
        mSkinsDao.searchAddSkins(idHero, search).flowOn(Dispatchers.Default).conflate()


    //endorse feature
    suspend fun insertEndorse(insert: List<EndorseEntity>) = mSkinsDao.insertEndorse(insert)
    fun getEndorse(): Flow<List<EndorseEntity>> {
        return mSkinsDao.getEndorse()
    }
}