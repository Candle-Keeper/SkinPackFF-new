package id.candlekeeper.core.di

import androidx.room.Room
import com.google.android.gms.ads.AdRequest
import id.candlekeeper.core.BuildConfig
import id.candlekeeper.core.data.source.local.LocalDataSource
import id.candlekeeper.core.data.source.local.room.SkinsDatabase
import id.candlekeeper.core.data.source.remote.RemoteDataSource
import id.candlekeeper.core.data.source.remote.network.ApiService
import id.candlekeeper.core.domain.AppRepository
import id.candlekeeper.core.domain.IAppRepository
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.core.utils.function.SSLCertificateConfigurator
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

val databaseModule = module {
    factory { get<SkinsDatabase>().skinsDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("codext".toCharArray())
        val factory = SupportFactory(passphrase)

        Room.databaseBuilder(
            androidContext(),
            SkinsDatabase::class.java, BuildConfig.DB_LOCAL_NAME
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val timeOut = 60L

        val trustManagerFactory = SSLCertificateConfigurator.getTrustManager(androidContext())
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException(
                "Unexpected default trust managers:" + Arrays.toString(
                    trustManagers
                )
            )
        }
        val trustManager = trustManagers[0] as X509TrustManager

        OkHttpClient.Builder()
//            .sslSocketFactory(
//                SSLCertificateConfigurator.getSSLConfiguration(androidContext()).socketFactory,
//                trustManager
//            )
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IAppRepository> { AppRepository(get(), get(), get()) }
}

val prefManagerModule = module {
    single { PrefManager(androidContext()) }
}

val adsRequestModule = module {
    single { AdRequest.Builder().build() }
}