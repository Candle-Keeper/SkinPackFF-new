package id.candlekeeper.core.utils

import id.candlekeeper.core.data.source.local.entity.dataContent.*
import id.candlekeeper.core.data.source.local.entity.dataIncome.EndorseEntity
import id.candlekeeper.core.data.source.remote.response.DataAdditionalResponse
import id.candlekeeper.core.data.source.remote.response.DataBaseAppResponse
import id.candlekeeper.core.data.source.remote.response.DataContentResponse
import id.candlekeeper.core.data.source.remote.response.DataIncomeResponse
import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.domain.model.dataBaseApp.AppStatus
import id.candlekeeper.core.domain.model.dataBaseApp.BaseApp
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.domain.model.dataIncome.Endorse


object DataMapper {

    //condition get collection
    private val appCondition = if (isDebug()) "debug" else "release"

    //DataAdditional ( res to mod )
    fun mapUrlTutorialResToMod(input: List<DataAdditionalResponse>): List<UrlTutorial> {
        return input.map {
            UrlTutorial(
                it.id_url_tutorial,
                it.name!!,
                it.youtube_url
            )
        }
    }

    //DataBaseApp ( res to mod )
    fun mapAppStatusResToMod(input: List<DataBaseAppResponse>): List<AppStatus> {
        val result = mutableListOf<AppStatus>()
        input.map {
            if (it.type == appCondition) {
                result.add(
                    AppStatus(
                        it.id_app_status!!,
                        it.version!!,
                        it.version_code!!,
                        it.is_server_down,
                    )
                )
            }
        }
        return result
    }

    fun mapBaseAppResToMod(input: DataBaseAppResponse): BaseApp {
        return BaseApp(
            input.token,
            input.base_files
        )
    }

    fun mapRequestMethodResToMod(input: List<DataBaseAppResponse>): List<RequestMethod> {
        val result = mutableListOf<RequestMethod>()
        input.map {
            if (it.type == appCondition) {
                result.add(
                    RequestMethod(
                        it.id_request_method!!,
                        it.name!!,
                        it.is_online_request
                    )
                )
            }
        }
        return result
    }

    //DataContent ( res to mod )
    fun mapCarouselResToMod(input: List<DataContentResponse>): List<Carousel> {
        return input.map {
            Carousel(
                it.id_content!!,
                it.name!!,
                it.image_url,
                it.link_url,
                it.activity_url
            )
        }
    }
    fun mapSkinsResToMod(input: List<DataContentResponse>): List<Skins> {
        return input.map {
            Skins(
                it.id_script!!,
                it.id_heroes,
                it.name!!,
                it.image_url,
                it.release,
                it.size,
                it.file_url,
                it.youtube_url,
                isHide = true
            )
        }
    }

    //DataIncome ( res to mod )
    fun mapAdmobResToMod(input: List<DataIncomeResponse>): List<Admob> {
        val result = mutableListOf<Admob>()
        input.map {
            if (it.type == appCondition) {
                result.add(
                    Admob(
                        it.id_admob!!,
                        it.name!!,
                        it.is_enable,
                        it.is_multiple_load,
                        it.ads_id_admob!!,
                        it.ads_id_applovin,
                        it.ads_id_applovin_small,
                        it.ads_id_startio,
                        it.ads_id_startio_small,
                        it.count_loop,
                        it.type_ads_banner
                    )
                )
            }
        }
        return result
    }


    //DataContent (map to ent)
    fun mapHeroesResToEnt(input: List<DataContentResponse>): List<HeroesEntity> {
        return input.map {
            HeroesEntity(
                it.id_heroes!!,
                it.id_category!!,
                it.name,
                it.image_url!!
            )
        }
    }

    fun mapSkinsResToEnt(input: List<DataContentResponse>): List<SkinsEntity> {
        return input.map {
            SkinsEntity(
                it.id_script,
                it.id_heroes!!,
                it.name!!,
                it.image_url!!,
                it.release,
                it.size,
                it.file_url,
                it.youtube_url,
                isHide = false
            )
        }
    }


    //DataIncome (map to ent)
    fun mapEndorseResToEnt(input: List<DataIncomeResponse>): List<EndorseEntity> {
        val result = mutableListOf<EndorseEntity>()
        input.map {
            if (it.type == appCondition) {
                result.add(
                    EndorseEntity(
                        it.id_endorse!!,
                        it.name!!,
                        it.title,
                        it.is_enable,
                        it.is_multiple_load,
                        it.description,
                        it.showing_index,
                        it.package_app,
                        it.image_url,
                        it.youtube_url,
                        it.activity_url,
                        it.web_url,
                    )
                )
            }
        }
        return result
    }


    //DataContent (ent to mod)
    fun mapHeroesEntToMod(input: List<HeroesEntity>): List<Heroes> {
        return input.map {
            Heroes(
                it.idHeroes,
                it.idCategory,
                it.name!!,
                it.imageUrl!!
            )
        }
    }

    fun mapSkinsEntToMod(input: List<SkinsEntity>): List<Skins> {
        return input.map {
            Skins(
                it.idScript!!,
                it.idHeroes,
                it.name,
                it.imageUrl,
                it.release,
                it.size,
                it.fileUrl,
                it.youtubeUrl,
                isHide = false
            )
        }
    }


    //DataIncome (ent to mod)
    fun mapEndorseEntToMod(input: List<EndorseEntity>): List<Endorse> {
        return input.map {
            Endorse(
                it.id,
                it.name,
                it.title,
                it.isEnable,
                it.isMultipleLoad,
                it.description,
                it.showingIndex,
                it.packageApp,
                it.imageUrl,
                it.youtubeUrl,
                it.activityUrl,
                it.webUrl
            )
        }
    }
}