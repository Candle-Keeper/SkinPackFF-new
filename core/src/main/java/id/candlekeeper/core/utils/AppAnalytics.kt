package id.candlekeeper.core.utils

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

object AppAnalytics {
    private const val ACTION_INPUT = "INPUT_"
    private const val ACTION_CLICK = "CLICK_"
    private const val ACTION_DOWNLOAD = "DOWNLOAD_"
    private const val ACTION_LOG = "LOG_"

    fun trackClick(event: String, param2: String = "") {
        track(event, ACTION_CLICK, param2)
    }

    fun trackInput(event: String, param2: String = "") {
        track(event, ACTION_INPUT, param2)
    }

    fun trackDownload(event: String, param2: String = "") {
        track(event, ACTION_DOWNLOAD, param2)
    }

    fun trackLog(event: String, param1: String, param2: String) {
        track(event, param1, param2)
    }

    private fun track(event: String, param1: String, param2: String = "") {
        Firebase.analytics.logEvent(event) {
            param("param1", param1)
            param("param2", param2)
        }
    }

    object Const {

        //Home Activity
        const val ADDITIONAL_PACK = ACTION_CLICK + "additional_pack"
        const val CUSTOM_PACK = ACTION_CLICK + "custom_pack"
        const val POPULAR_SKINS = ACTION_CLICK + "popular_skins"
        const val CAROUSEL = ACTION_CLICK + "carousel"

        //Detail Activity
        const val YOUTUBE = ACTION_DOWNLOAD + "youtube"
        const val YOUTUBE_TUTORIAL = ACTION_DOWNLOAD + "youtube_tutorial"
        const val YOUTUBE_UNINSTALL = ACTION_DOWNLOAD + "youtube_uninstall"
        const val DOWNLOAD = ACTION_DOWNLOAD + "download_pack"
        const val INSTALL = ACTION_DOWNLOAD + "instal_pack"
        const val CANCEL = ACTION_DOWNLOAD + "cancel_pack"

        //Request
        const val REQUEST = ACTION_CLICK + "add_request_skins"

        //More
        const val CHANGE_LANG = ACTION_CLICK + "change_language"
        const val CHANGE_THEME = ACTION_CLICK + "change_Theme"
        const val SHARE_APP = ACTION_CLICK + "share_app"
        const val GIVE_RATING = ACTION_CLICK + "give_rating"
        const val ADD_FEEDBACK = ACTION_CLICK + "add_feedback"

        //endorse
        const val ENDORSE_DIALOG_I = ACTION_CLICK + "endorse_dialog_install"
        const val ENDORSE_DIALOG_C = ACTION_CLICK + "endorse_dialog_click"

        const val ENDORSE_HEROLIST_I = ACTION_CLICK + "endorse_hero_list_install"
        const val ENDORSE_HEROLIST_C = ACTION_CLICK + "endorse_hero_list_click"

        const val ENDORSE_SKINSLIST_I = ACTION_CLICK + "endorse_skins_list_install"
        const val ENDORSE_SKINSLIST_C = ACTION_CLICK + "endorse_skins_list_click"

        const val ENDORSE_HOME1_I = ACTION_CLICK + "endorse_home1_install"
        const val ENDORSE_HOME1_C = ACTION_CLICK + "endorse_home1_click"

        const val ENDORSE_HOME2_I = ACTION_CLICK + "endorse_home2_install"
        const val ENDORSE_HOME2_C = ACTION_CLICK + "endorse_home2_click"
    }
}