package id.candlekeeper.core.domain

import id.candlekeeper.core.data.NetworkBoundResource
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.local.LocalDataSource
import id.candlekeeper.core.data.source.remote.RemoteDataSource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.data.source.remote.response.DataContentResponse
import id.candlekeeper.core.data.source.remote.response.DataIncomeResponse
import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.domain.model.dataBaseApp.AppStatus
import id.candlekeeper.core.domain.model.dataBaseApp.BaseApp
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Category
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.DataMapper
import id.candlekeeper.core.utils.FirestoreMethodClass
import id.candlekeeper.core.utils.PrefManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.RequestBody


interface IAppRepository {
    //Data Additional Feature
    fun getUrlTutorial(): Flow<ApiResponse<List<UrlTutorial>>>

    //Data BaseApp Feature
    fun getAppStatus(): Flow<ApiResponse<List<AppStatus>>>
    fun getRequestMethod(): Flow<ApiResponse<List<RequestMethod>>>
    fun getDinToken(): Flow<ApiResponse<BaseApp>>

    //Data Content Feature
    fun getCarousel(): Flow<ApiResponse<List<Carousel>>>
    fun getCategory(): Flow<Resource<List<Category>>>
    fun getHeroes(idCategory: Int): Flow<Resource<List<Heroes>>>
    fun getSkins(idHero: Int): Flow<Resource<List<Skins>>>
    fun getSkinsIsHide(): Flow<ApiResponse<List<Skins>>>
    fun searchHeroes(idCategory: Int, search: String): Flow<List<Heroes>>
    fun searchSkins(idHero: Int, search: String): Flow<List<Skins>>

    //Data Additional Content Feature
    fun getAddCategory(): Flow<Resource<List<Category>>>
    fun getAddHeroes(idCategory: Int): Flow<Resource<List<Heroes>>>
    fun getAddSkins(idHero: Int): Flow<Resource<List<Skins>>>
    fun getAllAddSkinsDb(): Flow<List<Skins>>
    fun searchAddHeroes(idCategory: Int, search: String): Flow<List<Heroes>>
    fun searchAddSkins(idHero: Int, search: String): Flow<List<Skins>>

    //Data Income Feature
    fun getAdsAdmob(): Flow<ApiResponse<List<Admob>>>
    fun getEndorse(): Flow<Resource<List<Endorse>>>

    //Data Income Local Only
    fun getEndorseDb(): Flow<List<Endorse>>

    //Data Monitor Feature
    fun pushMonitor(dataMonitor: HashMap<String, RequestBody>): Flow<ApiResponse<Boolean>>
}

class AppRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val prefManager: PrefManager
) : IAppRepository {


    //Data Additional Feature
    override fun getUrlTutorial(): Flow<ApiResponse<List<UrlTutorial>>> {
        return remoteDataSource.getUrlTutorial()
    }


    //Data BaseApp Feature
    override fun getAppStatus(): Flow<ApiResponse<List<AppStatus>>> {
        return remoteDataSource.getAppStatus()
    }

    override fun getRequestMethod(): Flow<ApiResponse<List<RequestMethod>>> {
        return remoteDataSource.getRequestMethod()
    }

    override fun getDinToken(): Flow<ApiResponse<BaseApp>> {
        return remoteDataSource.getDinToken()
    }


    //Data Content Feature
    override fun getCarousel(): Flow<ApiResponse<List<Carousel>>> {
        return remoteDataSource.getCarousel()
    }

    override fun getCategory(): Flow<Resource<List<Category>>> =
        object : NetworkBoundResource<List<Category>, List<DataContentResponse>>() {
            override fun loadFromDB(): Flow<List<Category>> {
                return localDataSource.getCategory().map {
                    DataMapper.mapCategoryEntToMod(it)
                }
            }

            override fun shouldFetch(data: List<Category>?): Boolean {
                return if (FirestoreMethodClass.category(prefManager)) {
                    true
                } else {
                    data == null || data.isEmpty()
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DataContentResponse>>> {
                return remoteDataSource.getDataContentAdvance(RemoteDataSource.CONT_CATEGORY)
            }

            override suspend fun saveCallResult(data: List<DataContentResponse>) {
                val result = DataMapper.mapCategoryResToEnt(data)
                localDataSource.insertCategory(result)
            }
        }.asFlow()

    override fun getHeroes(idCategory: Int): Flow<Resource<List<Heroes>>> =
        object : NetworkBoundResource<List<Heroes>, List<DataContentResponse>>() {
            override fun loadFromDB(): Flow<List<Heroes>> {
                return localDataSource.getHeroes(idCategory).map {
                    DataMapper.mapHeroesEntToMod(it)
                }
            }

            override fun shouldFetch(data: List<Heroes>?): Boolean {
                return if (FirestoreMethodClass.heroes(prefManager)) {
                    true
                } else {
                    data == null || data.isEmpty()
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DataContentResponse>>> {
                return remoteDataSource.getDataContentAdvance(
                    RemoteDataSource.CONT_HEROES,
                    idCategory
                )
            }

            override suspend fun saveCallResult(data: List<DataContentResponse>) {
                val result = DataMapper.mapHeroesResToEnt(data)
                localDataSource.insertHeroes(result)
            }
        }.asFlow()

    override fun getSkins(idHero: Int): Flow<Resource<List<Skins>>> =
        object : NetworkBoundResource<List<Skins>, List<DataContentResponse>>() {
            override fun loadFromDB(): Flow<List<Skins>> {
                return localDataSource.getSkins(idHero).map {
                    DataMapper.mapSkinsEntToMod(it)
                }
            }

            override fun shouldFetch(data: List<Skins>?): Boolean {
                return if (FirestoreMethodClass.skins(prefManager)) {
                    true
                } else {
                    data == null || data.isEmpty()
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DataContentResponse>>> {
                return remoteDataSource.getDataContentAdvance(
                    RemoteDataSource.CONT_SKINS,
                    0,
                    idHero
                )
            }

            override suspend fun saveCallResult(data: List<DataContentResponse>) {
                val result = DataMapper.mapSkinsResToEnt(data)
                localDataSource.insertSkins(result)
            }
        }.asFlow()

    override fun getSkinsIsHide(): Flow<ApiResponse<List<Skins>>> {
        return remoteDataSource.getSkinsIsHide()
    }

    override fun searchHeroes(idCategory: Int, search: String): Flow<List<Heroes>> {
        return localDataSource.searchHeroes(idCategory, search).map {
            DataMapper.mapHeroesEntToMod(it)
        }
    }

    override fun searchSkins(idHero: Int, search: String): Flow<List<Skins>> {
        return localDataSource.searchSkins(idHero, search).map {
            DataMapper.mapSkinsEntToMod(it)
        }
    }


    //Data Additonal Content Feature
    override fun getAddCategory(): Flow<Resource<List<Category>>> =
        object : NetworkBoundResource<List<Category>, List<DataContentResponse>>() {
            override fun loadFromDB(): Flow<List<Category>> {
                return localDataSource.getAddCategory().map {
                    DataMapper.mapAddCategoryEntToMod(it)
                }
            }

            override fun shouldFetch(data: List<Category>?): Boolean {
                return if (FirestoreMethodClass.addCategory(prefManager)) {
                    true
                } else {
                    data == null || data.isEmpty()
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DataContentResponse>>> {
                return remoteDataSource.getDataContentAdvance(RemoteDataSource.CONT_ADD_CATEGORY)
            }

            override suspend fun saveCallResult(data: List<DataContentResponse>) {
                val result = DataMapper.mapAddCategoryResToEnt(data)
                localDataSource.insertAddCategory(result)
            }
        }.asFlow()

    override fun getAddHeroes(idCategory: Int): Flow<Resource<List<Heroes>>> =
        object : NetworkBoundResource<List<Heroes>, List<DataContentResponse>>() {
            override fun loadFromDB(): Flow<List<Heroes>> {
                return localDataSource.getAddHeroes(idCategory).map {
                    DataMapper.mapAddHeroesEntToMod(it)
                }
            }

            override fun shouldFetch(data: List<Heroes>?): Boolean {
                return if (FirestoreMethodClass.addHeroes(prefManager)) {
                    true
                } else {
                    data == null || data.isEmpty()
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DataContentResponse>>> {
                return remoteDataSource.getDataContentAdvance(
                    RemoteDataSource.CONT_ADD_HEROES,
                    idCategory
                )
            }

            override suspend fun saveCallResult(data: List<DataContentResponse>) {
                val result = DataMapper.mapAddHeroesResToEnt(data)
                localDataSource.insertAddHeroes(result)
            }
        }.asFlow()

    override fun getAddSkins(idHero: Int): Flow<Resource<List<Skins>>> =
        object : NetworkBoundResource<List<Skins>, List<DataContentResponse>>() {
            override fun loadFromDB(): Flow<List<Skins>> {
                return localDataSource.getAddSkins(idHero).map {
                    DataMapper.mapAddSkinsEntToMod(it)
                }
            }

            override fun shouldFetch(data: List<Skins>?): Boolean {
                return if (FirestoreMethodClass.addSkins(prefManager)) {
                    true
                } else {
                    data == null || data.isEmpty()
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DataContentResponse>>> {
                return remoteDataSource.getDataContentAdvance(
                    RemoteDataSource.CONT_ADD_SKINS,
                    0,
                    idHero
                )
            }

            override suspend fun saveCallResult(data: List<DataContentResponse>) {
                val result = DataMapper.mapAddSkinsResToEnt(data)
                localDataSource.insertAddSkins(result)
            }
        }.asFlow()

    override fun getAllAddSkinsDb(): Flow<List<Skins>> {
        return localDataSource.getAllAddSkins().map {
            DataMapper.mapAddSkinsEntToMod(it)
        }
    }

    override fun searchAddHeroes(idCategory: Int, search: String): Flow<List<Heroes>> {
        return localDataSource.searchAddHeroes(idCategory, search).map {
            DataMapper.mapAddHeroesEntToMod(it)
        }
    }

    override fun searchAddSkins(idHero: Int, search: String): Flow<List<Skins>> {
        return localDataSource.searchAddSkins(idHero, search).map {
            DataMapper.mapAddSkinsEntToMod(it)
        }
    }


    //Data Income Feature
    override fun getAdsAdmob(): Flow<ApiResponse<List<Admob>>> {
        return remoteDataSource.getAdsAdmob()
    }

    override fun getEndorse(): Flow<Resource<List<Endorse>>> =
        object : NetworkBoundResource<List<Endorse>, List<DataIncomeResponse>>() {
            override fun loadFromDB(): Flow<List<Endorse>> {
                return localDataSource.getEndorse().map {
                    DataMapper.mapEndorseEntToMod(it)
                }
            }

            override fun shouldFetch(data: List<Endorse>?): Boolean {
                return if (FirestoreMethodClass.endorse(prefManager)) {
                    true
                } else {
                    data == null || data.isEmpty()
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<DataIncomeResponse>>> {
                return remoteDataSource.getEndorse()
            }

            override suspend fun saveCallResult(data: List<DataIncomeResponse>) {
                val result = DataMapper.mapEndorseResToEnt(data)
                localDataSource.insertEndorse(result)
            }
        }.asFlow()

    override fun getEndorseDb(): Flow<List<Endorse>> {
        return localDataSource.getEndorse().map {
            DataMapper.mapEndorseEntToMod(it)
        }
    }


    //Data Monitor Feature
    override fun pushMonitor(dataMonitor: HashMap<String, RequestBody>): Flow<ApiResponse<Boolean>> {
        return remoteDataSource.pushMonitor(dataMonitor)
    }

}