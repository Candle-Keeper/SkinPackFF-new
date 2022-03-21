package id.candlekeeper.skinpackml.ui.splash

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.startapp.sdk.ads.splash.SplashConfig
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.model.dataBaseApp.BaseApp
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.getIdSplashAd
import id.candlekeeper.skinpackml.R
import id.candlekeeper.skinpackml.databinding.ActivitySplashBinding
import id.candlekeeper.skinpackml.ui.dialog.DialogNoConnection
import id.candlekeeper.skinpackml.ui.home.HomeActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val prefManager: PrefManager by inject()
    private val viewModel: SplashViewModel by viewModel()

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataSplashAd = getIdSplashAd(prefManager)
        if (dataSplashAd.isEnable!!) {
            when (checkTheme(this)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    splashAdsDark(savedInstanceState)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    splashAdsLight(savedInstanceState)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    splashAdsLight(savedInstanceState)
                }
            }
        } else {
            StartAppSDK.init(this, resources.getString(R.string.app_id_startio), false)
            setContentView(binding.root)
        }

        observeData()
        hideStatusBar(this)
    }

    private fun splashAdsDark(savedInstanceState: Bundle?) {
        StartAppAd.showSplash(
            this, savedInstanceState,
            SplashConfig()
                .setTheme(SplashConfig.Theme.GLOOMY)
                .setCustomScreen(R.layout.activity_splash)
        )
    }

    private fun splashAdsLight(savedInstanceState: Bundle?) {
        StartAppAd.showSplash(
            this, savedInstanceState,
            SplashConfig()
                .setTheme(SplashConfig.Theme.ASHEN_SKY)
                .setCustomScreen(R.layout.activity_splash)
        )
    }

    private fun observeData() {
        viewModel.getDinToken().observe(this, dinToken)
    }

    private val dinToken = Observer<ApiResponse<BaseApp>> { data ->
        when (data) {
            is ApiResponse.Loading -> {
            }
            is ApiResponse.Success -> {
                prefManager.spBaseFiles = data.data.baseFiles
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
            is ApiResponse.Error -> {
                showError()
            }
            is ApiResponse.Empty -> {
                showError()
            }
        }
    }

    private fun showError() {
        toast(resources.getString(R.string.mohon_cek_koneksi))

        DialogNoConnection.build {
            observeData()
        }.show(supportFragmentManager, tag(this))
    }
}