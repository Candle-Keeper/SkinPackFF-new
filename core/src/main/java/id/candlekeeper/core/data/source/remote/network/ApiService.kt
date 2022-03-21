package id.candlekeeper.core.data.source.remote.network

import id.candlekeeper.core.BuildConfig
import id.candlekeeper.core.data.source.remote.response.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /*---- Data Base App----*/
    @POST("token")
    suspend fun getDinToken(
        @Header("Authorization") token: String = BuildConfig.API_KEY
    ): Response<DataBaseAppResponse>

    @GET("app_status")
    suspend fun getAppStatus(
        @Header("Authorization") token: String
    ): Response<ListDataBaseAppResponse>

    @GET("request_method")
    suspend fun getReqMethod(
        @Header("Authorization") token: String
    ): Response<ListDataBaseAppResponse>


    /*---- Data Content----*/
    @GET("carousel")
    suspend fun getCarousel(
        @Header("Authorization") token: String
    ): Response<ListDataContentResponse>

    @GET("category")
    suspend fun getCategory(
        @Header("Authorization") token: String
    ): Response<ListDataContentResponse>

    @GET("heroes/{idCategory}")
    suspend fun getHeroes(
        @Header("Authorization") token: String,
        @Path("idCategory") idCategory: Int
    ): Response<ListDataContentResponse>

    @GET("script/{idHero}")
    suspend fun getSkins(
        @Header("Authorization") token: String,
        @Path("idHero") idHero: Int
    ): Response<ListDataContentResponse>

    @GET("script")
    suspend fun getAllSkins(
        @Header("Authorization") token: String
    ): Response<ListDataContentResponse>

    @GET("additional/category")
    suspend fun getAdditionalCategory(
        @Header("Authorization") token: String
    ): Response<ListDataContentResponse>

    @GET("additional/heroes/{idCategory}")
    suspend fun getAdditionalHeroes(
        @Header("Authorization") token: String,
        @Path("idCategory") idCategory: Int
    ): Response<ListDataContentResponse>

    @GET("additional/script/{idHero}")
    suspend fun getAdditionalSkins(
        @Header("Authorization") token: String,
        @Path("idHero") idHero: Int
    ): Response<ListDataContentResponse>


    /*---- Data Income----*/
    @GET("admob")
    suspend fun getAdmob(
        @Header("Authorization") token: String
    ): Response<ListDataIncomeResponse>

    @GET("endorse")
    suspend fun getEndorse(
        @Header("Authorization") token: String
    ): Response<ListDataIncomeResponse>


    /*---- Data Additional----*/
    @GET("url_tutorial")
    suspend fun getUrlTutorial(
        @Header("Authorization") token: String
    ): Response<ListDataAdditionalResponse>


    /*---- Data Monitor----*/
    @JvmSuppressWildcards
    @Multipart
    @POST("monitoring")
    suspend fun pushMonitoring(
        @Header("Authorization") token: String,
        @PartMap dataMonitor: Map<String, RequestBody>
    ): Response<JSONObject>
}