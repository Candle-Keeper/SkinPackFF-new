package id.candlekeeper.core.data.source.remote

import  id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.data.source.remote.network.ApiService
import id.candlekeeper.core.data.source.remote.response.DataContentResponse
import id.candlekeeper.core.data.source.remote.response.DataIncomeResponse
import id.candlekeeper.core.data.source.remote.response.ListDataContentResponse
import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.domain.model.dataBaseApp.AppStatus
import id.candlekeeper.core.domain.model.dataBaseApp.BaseApp
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import retrofit2.Response
import java.io.IOException

class RemoteDataSource(private val apiService: ApiService) {

    //use case data content advance
    var token: String? = ""

    companion object {
        const val CONT_CATEGORY = "category"
        const val CONT_HEROES = "heroes"
        const val CONT_SKINS = "skins"
        const val CONT_ADD_CATEGORY = "add_category"
        const val CONT_ADD_HEROES = "add_heroes"
        const val CONT_ADD_SKINS = "add_skins"
    }


    //Data Additional Feature
    fun getUrlTutorial(): Flow<ApiResponse<List<UrlTutorial>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getUrlTutorial(token!!)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(DataMapper.mapUrlTutorialResToMod(response.body()!!.data)))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }


    //Data BaseApp Feature
    fun getAppStatus(): Flow<ApiResponse<List<AppStatus>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getAppStatus(token!!)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(DataMapper.mapAppStatusResToMod(response.body()!!.data)))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getRequestMethod(): Flow<ApiResponse<List<RequestMethod>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getReqMethod(token!!)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(DataMapper.mapRequestMethodResToMod(response.body()!!.data)))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDinToken(): Flow<ApiResponse<BaseApp>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getDinToken()
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(DataMapper.mapBaseAppResToMod(response.body()!!)))
                    token = response.body()!!.token
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }


    //Data Content Feature
    suspend fun getDataContentAdvance(
        from: String,
        idCategory: Int? = 0,
        idHero: Int? = 0
    ): Flow<ApiResponse<List<DataContentResponse>>> {

        suspend fun response(): Response<ListDataContentResponse> {
            return when (from) {
                CONT_CATEGORY -> return apiService.getCategory(token!!)
                CONT_HEROES -> return apiService.getHeroes(token!!, idCategory!!)
                CONT_SKINS -> return apiService.getSkins(token!!, idHero!!)
                CONT_ADD_CATEGORY -> return apiService.getAdditionalCategory(token!!)
                CONT_ADD_HEROES -> return apiService.getAdditionalHeroes(token!!, idCategory!!)
                CONT_ADD_SKINS -> return apiService.getAdditionalSkins(token!!, idHero!!)
                else -> apiService.getCategory(token!!)
            }
        }

        return flow {
            try {
                if (response().isSuccessful) {
                    emit(ApiResponse.Success(response().body()!!.data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getSkinsIsHide(): Flow<ApiResponse<List<Skins>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getAllSkins(token!!)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(DataMapper.mapSkinsResToMod(response.body()!!.data)))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getCarousel(): Flow<ApiResponse<List<Carousel>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getCarousel(token!!)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(DataMapper.mapCarouselResToMod(response.body()!!.data)))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }


    //Data Income Feature
    fun getAdsAdmob(): Flow<ApiResponse<List<Admob>>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.getAdmob(token!!)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(DataMapper.mapAdmobResToMod(response.body()!!.data)))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEndorse(): Flow<ApiResponse<List<DataIncomeResponse>>> {
        return flow {
            try {
                val response = apiService.getEndorse(token!!)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(response.body()!!.data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }


    //Data Monitor Feature
    fun pushMonitor(dataMonitor: HashMap<String, RequestBody>): Flow<ApiResponse<Boolean>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = apiService.pushMonitoring(token!!, dataMonitor)
                if (response.isSuccessful) {
                    emit(ApiResponse.Success(response.isSuccessful))
                } else {
                    emit(ApiResponse.Error(response.message()))
                }
            } catch (e: IOException) {
                emit(ApiResponse.Error(e.message!!))
            }
        }.flowOn(Dispatchers.IO)
    }
}
