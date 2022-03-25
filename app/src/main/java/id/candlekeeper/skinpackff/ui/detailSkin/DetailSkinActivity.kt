package id.candlekeeper.skinpackff.ui.detailSkin

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.ironsource.mediationsdk.IronSource
import com.startapp.sdk.ads.nativead.NativeAdDetails
import id.candlekeeper.core.adapter.CustomAdapter
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.databinding.ItemAdNativeStartioSmallBinding
import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.*
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.ActivityDetailSkinBinding
import id.candlekeeper.skinpackff.ui.dialog.DialogFeedback
import id.candlekeeper.skinpackff.ui.dialog.DialogInstallSkin
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class DetailSkinActivity : AppCompatActivity(), OnItemClicked,
    EasyPermissions.PermissionCallbacks {

    private val prefManager: PrefManager by inject()
    private val viewModel: DetailSkinViewModel by viewModel()

    private val bindingNativeStartIoSmall: ItemAdNativeStartioSmallBinding by lazy {
        ItemAdNativeStartioSmallBinding.inflate(layoutInflater)
    }
    private val binding: ActivityDetailSkinBinding by lazy {
        ActivityDetailSkinBinding.inflate(
            layoutInflater
        )
    }

    //adapter
    private lateinit var tutorialAdapter: CustomAdapter

    //native ads list
    private val adRequest: AdRequest by inject()
    private var dataList: MutableList<Any> = ArrayList()
    private val nativeAdList: MutableList<NativeAd> = ArrayList()

    //parcelize
    private var detailSkins: Skins? = null

    companion object {
        //auto generate data detail
        var RELEASE_DATE = ""
        var SIZE_FILE = ""

        //parcelize
        const val DETAIL_SKINS = "detail_skins"
    }

    //activity result permission
    private val handleIntentActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val directoryUri = it.data?.data ?: return@registerForActivityResult
                contentResolver.takePersistableUriPermission(
                    directoryUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                prefManager.spIsSAF = true
                toDownloadDialog()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //show dinamic ads interstisial
        showAdsDetailIntertisial(this, this, prefManager)
        setContentView(binding.root)

        initView()
        randomData()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
        setupView()
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
    }


    private fun initView() {
        detailSkins = intent.extras?.getParcelable(DETAIL_SKINS)
        hideStatusBar(this)

        //init recycler
        with(binding.rvTutorial) {
            isNestedScrollingEnabled = false
            tutorialAdapter = CustomAdapter(this@DetailSkinActivity, prefManager)
            val gridLayoutManager = GridLayoutManager(this@DetailSkinActivity, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = dataList[position]
                    return if (viewType is NativeAd || viewType is NativeAdDetails || viewType is MaxNativeAdView) {
                        2
                    } else 1
                }
            }

            layoutManager = gridLayoutManager
            adapter = tutorialAdapter
        }

        with(binding) {
            ivBack.setOnClickListener {
                onBackPressed()
            }
            ivDownload.setOnClickListener {
                AppAnalytics.trackDownload(AppAnalytics.Const.DOWNLOAD)
                if (isAboveAndroid11()) {
                    if (prefManager.spIsSAF) {
                        toDownloadDialog()
                    } else {
                        checkPermissionSaf()
                    }
                } else {
                    if (checkPermission10()) {
                        toDownloadDialog()
                    } else {
                        reqPermission10()
                    }
                }
            }
            ivYoutube.setOnClickListener {
                AppAnalytics.trackClick(AppAnalytics.Const.YOUTUBE)
                intentToYoutube(this@DetailSkinActivity, detailSkins?.youtubeUrl.toString())
            }
            llReport.setOnClickListener {
                DialogFeedback.fromPage = "detail"
                DialogFeedback.fileUrl = detailSkins?.fileUrl.toString()
                DialogFeedback.nameSkins = detailSkins?.name.toString()

                DialogFeedback.build() {
                    //show dinamic ads interstisial
                    showAdsAllDialogIntertisial(
                        this@DetailSkinActivity,
                        this@DetailSkinActivity,
                        prefManager
                    )
                }.show(supportFragmentManager, tag(this@DetailSkinActivity))
            }
        }
    }

    private fun setupView() {
        with(binding) {
            val dataBannerAds = getIdBannerDetail(prefManager)
            val dataNativeAds = getIdNativeDialog(prefManager)
            if (dataBannerAds.isEnable!!) { //banner admob
//                showAdsBanner(
//                    this@DetailSkinActivity,
//                    this@DetailSkinActivity,
//                    dataBannerAds,
//                    adsInclude.frameLayout,
//                    adRequest
//                )
            } else {
                setupAdsNativeApplovinSmall(
                    this@DetailSkinActivity,
                    bindingNativeStartIoSmall,
                    dataNativeAds,
                    this@DetailSkinActivity,
                    null,
                    adsInclude.frameLayout
                )
            }

            when (checkTheme(this@DetailSkinActivity)) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    ivAllSkin.loadImageRs(this@DetailSkinActivity, "bg_detail_skin_dark")
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    ivAllSkin.loadImageRs(this@DetailSkinActivity, "bg_detail_skin_light")
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    ivAllSkin.loadImageRs(this@DetailSkinActivity, "bg_detail_skin_light")
                }
            }

            ivBackground.loadImageFull(prefManager, detailSkins?.imageUrl.toString())
            ivYoutube.loadImageYt(detailSkins?.youtubeUrl.toString())

            if (isDebug()) {
                tvName.text = detailSkins!!.fileUrl
                tvName.setOnClickListener {
                    val i = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(prefManager.spBaseFiles + tvName.text.toString())
                    )
                    startActivity(i)
                }
            } else {
                tvName.text = detailSkins?.name
            }

            if (detailSkins?.release != "") {
                tvRelease.text = detailSkins?.release
            } else {
                tvRelease.text = RELEASE_DATE
            }

            if (detailSkins?.size != "") {
                tvSize.text = detailSkins?.size.toString()
            } else {
                tvSize.text = "$SIZE_FILE Mb"
            }
        }
    }

    private fun observeData() {
        viewModel.getUrlTutorial().observe(this) { data ->
            if (data != null) {
                when (data) {
                    is ApiResponse.Loading -> {
                    }
                    is ApiResponse.Success -> {
                        setupDataList(data.data)
                    }
                    is ApiResponse.Error -> {
                        toast(data.errorMessage)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun setupDataList(data: List<Any>) {
        dataList.clear()
        dataList.addAll(data)
        tutorialAdapter.addList(dataList)

        //setup native list
        val dataNativeAds = getIdNativeDetailList(prefManager)
        if (dataNativeAds.isEnable!!) {
            setupAdInRecycler(
                data.size,
                dataNativeAds,
                nativeAdList,
                this,
                dataList,
                adRequest,
                "detail",
                2,
                this
            ) {
                tutorialAdapter.addList(dataList)
            }
        }
    }

    private fun toDownloadDialog() {
        DialogInstallSkin.build(detailSkins!!) {
            finish()
            startActivity(intent)
        }.show(supportFragmentManager, tag(this))
    }

    private fun randomData() {
        SIZE_FILE = (5..12).random().toString()
        val randomRelease: Array<String> =
            arrayOf("25-10-2019", "24-09-2020", "21-08-2021", "22-08-2020", "22-09-2018")
        RELEASE_DATE = randomRelease.random()
    }


    override fun onEventClick(data: UrlTutorial) {
        super.onEventClick(data)
        intentToYoutube(this@DetailSkinActivity, data.youtubeUrl.toString())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkPermissionSaf() {
        contentResolver.persistedUriPermissions.find {
            prefManager.spIsSAF
//            it.uri.equals(uriPermission(2)) && it.isWritePermission
        }?.run { } ?: handleIntentActivityResult.launch(
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                .putExtra(
                    DocumentsContract.EXTRA_INITIAL_URI,
                    uriPermission(1)
                )
        )
    }

    private fun checkPermission10(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun reqPermission10() {
        EasyPermissions.requestPermissions(
            this, getString(R.string.acces),
            123,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        toDownloadDialog()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            reqPermission10()
        }
    }
}