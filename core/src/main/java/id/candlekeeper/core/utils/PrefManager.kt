package id.candlekeeper.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.securepreferences.SecurePreferences
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import java.lang.reflect.Type

private const val PREFS_NAME = "skinpackml"
private const val PREF_FIRESTORE_METHOD = "PREF_REQUEST_METHOD"
private const val PREF_ADS_ADMOB = "PREF_ADS_ADMOB"


class PrefManager(context: Context) {

    //    encrypt
    private val sp: SharedPreferences by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            val masterKey = MasterKey.Builder(context)
                .setKeyGenParameterSpec(spec)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            SecurePreferences(context, PREFS_NAME, PREFS_NAME)
        }
    }

//    no encrypt
//    private val sp: SharedPreferences by lazy {
//        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//    }

    val spe: SharedPreferences.Editor by lazy {
        sp.edit()
    }

    /* ==============================================PREF VAR================================================*/


    //Pref Thema
    var spTheme: Int
        get() = sp.getInt("spTheme", 3)
        set(value) {
            spe.putInt("spTheme", value)
            spe.apply()
        }

    //Pref Is Review
    var spIsRate: Boolean
        get() = sp.getBoolean("spIsRate", false)
        set(value) {
            spe.putBoolean("spIsRate", value)
            spe.apply()
        }

    //Pref Count Dialog Review
    fun clearCountValue() {
        sp.edit().remove("spCountRate").clear().apply()
        sp.edit().remove("spCountEndorse").clear().apply()
    }

    var spCountRate: Int
        get() = sp.getInt("spCountRate", 0)
        set(value) {
            spe.putInt("spCountRate", value)
            spe.apply()
        }

    //Pref Count Dialog Endorse
    var spCountEndorse: Int
        get() = sp.getInt("spCountEndorse", 0)
        set(value) {
            spe.putInt("spCountEndorse", value)
            spe.apply()
        }

    //pref package name ml
    var spPackageNameFF: String?
        get() = sp.getString("spPackageNameFF", packageFF)
        set(value) {
            spe.putString("spPackageNameFF", value)
            spe.apply()
        }

    //Pref Count Dialog Review
    var spIsSAF: Boolean
        get() = sp.getBoolean("spIsSAF", false)
        set(value) {
            spe.putBoolean("spIsSAF", value)
            spe.apply()
        }

    //Pref Count Download Skins
    var spCountDownloadSkins: Int
        get() = sp.getInt("spCountDownloadSkins", 1)
        set(value) {
            spe.putInt("spCountDownloadSkins", value)
            spe.apply()
        }

    //Pref Base Url Files
    var spBaseFiles: String?
        get() = sp.getString("spBaseFiles", "")
        set(value) {
            spe.putString("spBaseFiles", value)
            spe.apply()
        }


    /* ==============================================PREF OBJECT================================================*/

    //Pref AdsAdmob Banner
    fun spSaveAdsAdmob(list: List<Admob>) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        spe.putString(PREF_ADS_ADMOB, json)
        spe.apply()
    }

    fun spGetAdsAdmob(): List<Admob>? {
        val gson = Gson()
        val json: String? = sp.getString(PREF_ADS_ADMOB, "")
        val type: Type = object : TypeToken<List<Admob?>?>() {}.type
        return gson.fromJson(json, type)
    }

    //Pref Firebase Methode
    fun spSaveFirebaseMethod(list: List<RequestMethod>) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        spe.putString(PREF_FIRESTORE_METHOD, json)
        spe.apply()
    }

    fun spGetFirebaseMethod(): List<RequestMethod>? {
        val gson = Gson()
        val json: String? = sp.getString(PREF_FIRESTORE_METHOD, "")
        val type: Type = object : TypeToken<List<RequestMethod?>?>() {}.type
        return gson.fromJson(json, type)
    }

}
