package id.candlekeeper.skinpackff.ui.skinList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.ironsource.mediationsdk.IronSource
import com.startapp.sdk.ads.nativead.NativeAdDetails
import id.candlekeeper.core.adapter.CustomAdapter
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.getIdNativeSkinsList
import id.candlekeeper.core.utils.function.loadAdsActivityIntertisial
import id.candlekeeper.core.utils.function.setupAdInRecycler
import id.candlekeeper.core.utils.function.showAdsSKinsListIntertisial
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.ActivitySkinListBinding
import id.candlekeeper.skinpackff.ui.detailSkin.DetailSkinActivity
import id.candlekeeper.skinpackff.ui.detailSkin.DetailSkinActivity.Companion.DETAIL_SKINS
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SkinListActivity : AppCompatActivity(), OnItemClicked, View.OnClickListener {

    private val prefManager: PrefManager by inject()
    private val viewModel: SkinsViewModel by viewModel()

    private val binding: ActivitySkinListBinding by lazy {
        ActivitySkinListBinding.inflate(layoutInflater)
    }

    //Native Add
    private val adRequest: AdRequest by inject()
    private var dataList: MutableList<Any> = ArrayList()
    private val nativeAdList: MutableList<NativeAd> = ArrayList()

    //adapter
    private lateinit var skinsAdapter: CustomAdapter

    //parcelize
    private var heroes: Heroes? = null
    private var isHideSkins: Boolean? = false

    companion object {
        const val HEROES = "heroes"
        const val IS_HIDE_SKINS = "IS_HIDE_SKINS"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //show dinamic ads interstisial
        showAdsSKinsListIntertisial(this, this, prefManager)
        setContentView(binding.root)

        initView()
        observeSearchQuery()
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
        loadAdsActivityIntertisial(this, this, adRequest, prefManager)
    }

    private fun initView() {
        heroes = intent.extras?.getParcelable(HEROES)
        isHideSkins = intent.extras?.getBoolean(IS_HIDE_SKINS)


        //setup recycler
        with(binding.rvSkins) {
            isNestedScrollingEnabled = false
            skinsAdapter = CustomAdapter(this@SkinListActivity, prefManager)

            val gridLayoutManager = GridLayoutManager(this@SkinListActivity, 2)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = dataList[position]
                    return if (viewType is NativeAd || viewType is Endorse || viewType is NativeAdDetails || viewType is MaxNativeAdView) {
                        2
                    } else 1
                }
            }

            layoutManager = gridLayoutManager
            adapter = skinsAdapter
        }

        //init View
        with(binding) {
            ivBack.setOnClickListener(this@SkinListActivity)
        }

        when (isHideSkins) {
            true -> {
                viewModel.getSkinsIsHide().observe(this, hideSkins)
            }
            else -> {
                binding.tvSkin.text = "Hero " + heroes?.name
                viewModel.getSkins(heroes?.idHeroes!!).observe(this, skins)
            }
        }
    }


    private val skins = Observer<Resource<List<Skins>>> { data ->
        if (data != null) {
            when (data) {
                is Resource.Loading -> {
                    observeLoading()
                }

                is Resource.Success -> {
                    if (data.data.isNullOrEmpty()) {
                        observeNull()
                    }

                    observeSuccess()
                    setupDataList(data.data!!)
                }

                is Resource.Error -> {
                    observeError(data.message.toString())
                }
            }
        }
    }

    private val hideSkins = Observer<ApiResponse<List<Skins>>> { data ->
        if (data != null) {
            when (data) {
                is ApiResponse.Loading -> {
                    observeLoading()
                }

                is ApiResponse.Success -> {
                    if (data.data.isNullOrEmpty()) {
                        observeNull()
                    }

                    observeSuccess()
                    setupDataList(data.data)
                }

                is ApiResponse.Error -> {
                    observeError(data.errorMessage)
                }
            }
        }
    }

    private val searchSkins = Observer<List<Skins>> { data ->
        skinsAdapter.addList(data)
    }


    private fun observeSearchQuery() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (heroes?.isAdditional!!) {
                    viewModel.searchAddSkins(heroes?.idHeroes!!, newText!!)
                        .observe(this@SkinListActivity, searchSkins)
                } else {
                    viewModel.searchSkins(heroes?.idHeroes!!, newText!!)
                        .observe(this@SkinListActivity, searchSkins)
                }
                return true
            }
        })
    }

    private fun setupDataList(data: List<Any>) {
        dataList.clear()
        dataList.addAll(data)
        skinsAdapter.addList(dataList)

        setupEndorseList()

        //setup native ads
        val dataNativeAds = getIdNativeSkinsList(prefManager)
        if (dataNativeAds.isEnable!!) {
            setupAdInRecycler(
                data.size,
                dataNativeAds,
                nativeAdList,
                this,
                dataList,
                adRequest,
                "skins",
                3,
                this
            ) {
                skinsAdapter.addList(dataList)
            }
        }
    }

    private fun setupEndorseList() {
        viewModel.getEndorseDb().observe(this) {
            it.map { data ->
                if (data.name == "endorse_skins_list" && data.isEnable!!) {

                    if (dataList.isNotEmpty()) {
                        if (dataList.size > 1) {
                            if (data.isMultipleLoad!!) {
                                clearEndorse(dataList, 0)
                                clearEndorse(dataList, 3)
                                dataList.add(0, data)
                                dataList.add(3, data)
                            } else {
                                clearEndorse(dataList, 0)
                                dataList.add(0, data)
                            }
                        } else {
                            clearEndorse(dataList, 0)
                            dataList.add(0, data)
                        }
                    }
                    skinsAdapter.addList(dataList)
                }
            }
        }
    }


    private fun observeSuccess() {
        with(binding) {
            hideShimmer(mShimmerViewContainer, rvSkins)
        }
    }

    private fun observeNull() {
        with(binding) {
            toast(resources.getString(R.string.empty))
            rvSkins.hide()
            lootieEmpty.show()
            lootieEmpty(lootieEmpty)
        }
    }

    private fun observeLoading() {
        with(binding) {
            showShimmer(mShimmerViewContainer, rvSkins)
        }
    }

    private fun observeError(message: String) {
        with(binding) {
            showShimmer(mShimmerViewContainer, rvSkins)
        }
        toast(message)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> onBackPressed()
        }
    }

    override fun onEventClick(data: Skins) {
        super.onEventClick(data)

        Intent(this, DetailSkinActivity::class.java).let {
            it.putExtra(DETAIL_SKINS, data)
            startActivity(it)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onEventClick(data: Endorse) {
        super.onEventClick(data)
        if (isAppInstalled(packageChrome, this)) {
            intentCustomTabs(data.activityUrl!!, this)
        } else {
            intentToWeb(this, data.activityUrl!!)
        }
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
        binding.mShimmerViewContainer.stopShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeAdList.map { it.destroy() }
    }
}