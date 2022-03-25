package id.candlekeeper.skinpackff

import android.app.Application
import android.util.Log
import com.applovin.sdk.AppLovinPrivacySettings
import com.applovin.sdk.AppLovinSdk
import com.google.ads.mediation.inmobi.InMobiConsent
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.inmobi.sdk.InMobiSdk
import com.startapp.sdk.adsbase.StartAppSDK
import id.candlekeeper.core.di.*
import id.candlekeeper.core.utils.isDebug
import id.candlekeeper.skinpackff.di.useCaseModule
import id.candlekeeper.skinpackff.di.viewModelModule
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MyApp : Application() {

    companion object {
        @get:Synchronized
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()

        initOther()
        startKoin()
        initNotification()
    }

    private fun initOther() {
        instance = this

        FirebaseApp.initializeApp(this)
        MobileAds.initialize(this) {}

        //setup mediation appLovin
        AppLovinPrivacySettings.setHasUserConsent(true, this)

        // setup appLovin max
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk {}

        //setup start.io
        StartAppSDK.setUserConsent(this, "pas", System.currentTimeMillis(), true)
//        StartAppSDK.getExtras(this)
//            .edit()
//            .putString("IABUSPrivacy_String", "1YNN")
//            .apply()
        if (isDebug()) {
            StartAppSDK.setTestAdsEnabled(true)
        }

        // setup inmobi
        val consentObject = JSONObject()
        try {
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true)
            consentObject.put("gdpr", "1")
        } catch (exception: JSONException) {
        }
        InMobiConsent.updateGDPRConsent(consentObject)
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApp)
            modules(
                listOf(
                    networkModule,
                    prefManagerModule,
                    adsRequestModule,
                    databaseModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }

    private fun initNotification() {
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("fcm", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            Log.e("fcm", task.result)
        })

        fun initTopic(): String {
            if (isDebug()) {
                return "debug"
            }
            return "release"
        }

        Firebase.messaging.subscribeToTopic(initTopic())
    }
}