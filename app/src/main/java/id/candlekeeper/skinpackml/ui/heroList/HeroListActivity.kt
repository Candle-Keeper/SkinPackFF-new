package id.candlekeeper.skinpackml.ui.heroList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.ironsource.mediationsdk.IronSource
import id.candlekeeper.core.adapter.CustomAdapter
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.domain.model.dataContent.Category
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.getIdNativeHeroList
import id.candlekeeper.core.utils.function.setupAdInRecycler
import id.candlekeeper.core.utils.function.showAdsHeroListIntertisial
import id.candlekeeper.skinpackml.R
import id.candlekeeper.skinpackml.databinding.ActivityHeroListBinding
import id.candlekeeper.skinpackml.ui.skinList.SkinListActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HeroListActivity : AppCompatActivity(), OnItemClicked, View.OnClickListener {

    private val viewModel: HeroViewModel by viewModel()
    private val prefManager: PrefManager by inject()

    private val binding: ActivityHeroListBinding by lazy {
        ActivityHeroListBinding.inflate(layoutInflater)
    }

    //Native Add
    private val adRequest: AdRequest by inject()
    private var dataList = mutableListOf<Any>()
    private val nativeAdList = mutableListOf<NativeAd>()

    //adapter
    private lateinit var heroAdapter: CustomAdapter

    //parcelize
    private var category: Category? = null

    companion object {
        const val CATEGORY = "category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //show dinamic ads interstisial
        showAdsHeroListIntertisial(this, this, prefManager)
        setContentView(binding.root)

        initView()
        observeSearchQuery()
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
    }

    private fun initView() {
        category = intent.extras?.getParcelable(CATEGORY)

        //init view
        with(binding) {
            ivBack.setOnClickListener(this@HeroListActivity)
        }

        //init recycler
        with(binding.rvHeros) {
            isNestedScrollingEnabled = false
            heroAdapter = CustomAdapter(this@HeroListActivity, prefManager)
            layoutManager =
                GridLayoutManager(this@HeroListActivity, 1, GridLayoutManager.VERTICAL, false)
            adapter = heroAdapter
        }

        if (category?.isAdditional!!) {
            binding.tvRole.text = resources.getString(R.string.additional_pack)
            viewModel.getAddHeroes(category?.idCategory!!).observe(this, heroes)
        } else {
            binding.tvRole.text = category?.name
            viewModel.getHeroes(category?.idCategory!!).observe(this, heroes)
        }
    }

    private fun observeSearchQuery() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (category?.isAdditional!!) {
                        viewModel.searchAddHeroes(category?.idCategory!!, it)
                            .observe(this@HeroListActivity, searchHeroes)
                    } else {
                        viewModel.searchHeroes(category?.idCategory!!, it)
                            .observe(this@HeroListActivity, searchHeroes)
                    }
                }
                return true
            }
        })
    }

    private val heroes = Observer<Resource<List<Heroes>>> { data ->
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

    private val searchHeroes = Observer<List<Heroes>> { data ->
        heroAdapter.addList(data)
    }

    private fun setupDataList(data: List<Any>) {
        dataList.clear()
        dataList.addAll(data)
        heroAdapter.addList(dataList)

        setupEndorseList()

        //setup native ads
        val dataNativeAds = getIdNativeHeroList(prefManager)
        if (dataNativeAds.isEnable!!) {
            setupAdInRecycler(
                data.size,
                dataNativeAds,
                nativeAdList,
                this,
                dataList,
                adRequest,
                "heroes",
                2,
                this
            ) {
                heroAdapter.addList(dataList)
            }
        }
    }

    private fun setupEndorseList() {
        viewModel.getEndorseDb().observe(this) {
            it.map { data ->
                if (data.name == "endorse_hero_list" && data.isEnable!!) {

                    if (dataList.isNotEmpty()) {
                        if (dataList.size > 1) {
                            if (data.isMultipleLoad!!) {
                                clearEndorse(dataList, 0)
                                clearEndorse(dataList, 2)
                                dataList.add(0, data)
                                dataList.add(2, data)
                            } else {
                                clearEndorse(dataList, 0)
                                dataList.add(0, data)
                            }
                        } else {
                            clearEndorse(dataList, 0)
                            dataList.add(0, data)
                        }
                    }
                    heroAdapter.addList(dataList)
                }
            }
        }
    }

    private fun observeSuccess() {
        with(binding) {
            hideShimmer(mShimmerViewContainer, rvHeros)
        }
    }

    private fun observeNull() {
        with(binding) {
            toast(resources.getString(R.string.empty))
            rvHeros.hide()
            lootieEmpty.show()
            lootieEmpty(lootieEmpty)
        }
    }

    private fun observeLoading() {
        with(binding) {
            showShimmer(mShimmerViewContainer, rvHeros)
        }
    }

    private fun observeError(message: String) {
        with(binding) {
            showShimmer(mShimmerViewContainer, rvHeros)
        }
        toast(message)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> onBackPressed()
        }
    }

    override fun onEventClick(data: Heroes) {
        super.onEventClick(data)
        AppAnalytics.trackClick(data.name)

        Intent(this, SkinListActivity::class.java).let {
            it.putExtra(SkinListActivity.HEROES, data)
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