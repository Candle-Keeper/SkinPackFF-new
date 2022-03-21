package id.candlekeeper.core.domain

import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
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
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody


interface IAppUseCase {
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

class AppInteractor(private val iAppRepository: IAppRepository) : IAppUseCase {

    //Data Additional Feature
    override fun getUrlTutorial(): Flow<ApiResponse<List<UrlTutorial>>> {
        return iAppRepository.getUrlTutorial()
    }


    //Data BaseApp Feature
    override fun getDinToken(): Flow<ApiResponse<BaseApp>> {
        return iAppRepository.getDinToken()
    }

    override fun getAppStatus(): Flow<ApiResponse<List<AppStatus>>> =
        iAppRepository.getAppStatus()

    override fun getRequestMethod(): Flow<ApiResponse<List<RequestMethod>>> {
        return iAppRepository.getRequestMethod()
    }


    //Data Content Feature
    override fun getCarousel(): Flow<ApiResponse<List<Carousel>>> =
        iAppRepository.getCarousel()

    override fun getCategory(): Flow<Resource<List<Category>>> =
        iAppRepository.getCategory()

    override fun getHeroes(idCategory: Int): Flow<Resource<List<Heroes>>> =
        iAppRepository.getHeroes(idCategory)

    override fun getSkins(idHero: Int): Flow<Resource<List<Skins>>> =
        iAppRepository.getSkins(idHero)

    override fun getSkinsIsHide(): Flow<ApiResponse<List<Skins>>> {
        return iAppRepository.getSkinsIsHide()
    }

    override fun searchHeroes(idCategory: Int, search: String): Flow<List<Heroes>> =
        iAppRepository.searchHeroes(idCategory, search)

    override fun searchSkins(idHero: Int, search: String): Flow<List<Skins>> =
        iAppRepository.searchSkins(idHero, search)


    //Data Content Feature
    override fun getAddCategory(): Flow<Resource<List<Category>>> =
        iAppRepository.getAddCategory()

    override fun getAddHeroes(idCategory: Int): Flow<Resource<List<Heroes>>> =
        iAppRepository.getAddHeroes(idCategory)

    override fun getAddSkins(idHero: Int): Flow<Resource<List<Skins>>> =
        iAppRepository.getAddSkins(idHero)

    override fun getAllAddSkinsDb(): Flow<List<Skins>> {
        return iAppRepository.getAllAddSkinsDb()
    }

    override fun searchAddHeroes(idCategory: Int, search: String): Flow<List<Heroes>> =
        iAppRepository.searchAddHeroes(idCategory, search)

    override fun searchAddSkins(idHero: Int, search: String): Flow<List<Skins>> =
        iAppRepository.searchAddSkins(idHero, search)


    //Data Income Feature
    override fun getAdsAdmob(): Flow<ApiResponse<List<Admob>>> {
        return iAppRepository.getAdsAdmob()
    }

    override fun getEndorse(): Flow<Resource<List<Endorse>>> {
        return iAppRepository.getEndorse()
    }

    override fun getEndorseDb(): Flow<List<Endorse>> {
        return iAppRepository.getEndorseDb()
    }


    //Data Monitor Feature
    override fun pushMonitor(dataMonitor: HashMap<String, RequestBody>): Flow<ApiResponse<Boolean>> {
        return iAppRepository.pushMonitor(dataMonitor)
    }
}