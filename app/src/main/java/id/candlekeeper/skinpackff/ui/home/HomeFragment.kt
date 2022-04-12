package id.candlekeeper.skinpackff.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import id.candlekeeper.core.adapter.CarouselAdapter
import id.candlekeeper.core.adapter.CustomAdapter
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.model.dataBaseApp.AppStatus
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.getIdNativeCarousel
import id.candlekeeper.core.utils.function.getIdNativeHeroList
import id.candlekeeper.core.utils.function.loadAdsActivityIntertisial
import id.candlekeeper.core.utils.function.setupAdInRecycler
import id.candlekeeper.skinpackff.BuildConfig
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.FragmentHomeBinding
import id.candlekeeper.skinpackff.ui.detailSkin.DetailSkinActivity
import id.candlekeeper.skinpackff.ui.dialog.DialogAds
import id.candlekeeper.skinpackff.ui.dialog.DialogChooseMl
import id.candlekeeper.skinpackff.ui.dialog.DialogServerError
import id.candlekeeper.skinpackff.ui.dialog.DialogUpdateApp
import id.candlekeeper.skinpackff.ui.skinList.SkinListActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), OnItemClicked {

    private var _homeFragment: FragmentHomeBinding? = null
    private val binding get() = _homeFragment as FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModel()
    private val prefManager: PrefManager by inject()

    //Native Carousel
    private val adRequest: AdRequest by inject()
    private var dataListCarousel = mutableListOf<Any>()
    private val nativeAdListCarousel = mutableListOf<NativeAd>()

    //Native Content
    private var dataListContent = mutableListOf<Any>()
    private val nativeAdListContent = mutableListOf<NativeAd>()

    //carousel
    private lateinit var viewPager: ViewPager2
    private lateinit var carouselAdapter: CarouselAdapter

    //autoScroll
    private var scrollHandler = Handler(Looper.getMainLooper())
    private val SCROLL_DELAY = 5000L
    private var scrollRunnable = Runnable {
        val setCurrent = binding.rvCarousel.currentItem + 1
        binding.rvCarousel.currentItem = setCurrent

        if ((setCurrent) == carouselAdapter.itemCount) {
            binding.rvCarousel.currentItem = 0
        }
    }

    //adapter
    private lateinit var heroesAdapter: CustomAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _homeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showTypegame()
        initView()
        setList()
        setupRateView()
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    override fun onPause() {
        super.onPause()
        scrollHandler.removeCallbacksAndMessages(null)
        binding.mShimmerViewContainer.stopShimmer()
    }

    private fun showTypegame() {
        AppAnalytics.trackDownload(AppAnalytics.Const.INSTALL)
        DialogChooseMl {}.show(requireFragmentManager(), tag(requireContext()))
    }


    private fun initView() {
        //set statusbar color
        when (checkTheme(requireContext())) {
            Configuration.UI_MODE_NIGHT_NO -> {
                (activity as HomeActivity?)?.updateStatusBarColor("#FFFFFFFF")
                WindowInsetsControllerCompat(
                    activity?.window!!,
                    activity?.window?.decorView!!
                ).isAppearanceLightStatusBars = true
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                (activity as HomeActivity?)?.updateStatusBarColor("#FF000000")
                WindowInsetsControllerCompat(
                    activity?.window!!,
                    activity?.window?.decorView!!
                ).isAppearanceLightStatusBars = false
            }
        }

        //clear count event
        if (prefManager.spCountRate >= 1000 && prefManager.spCountEndorse >= 1000) {
            prefManager.clearCountValue()
        }

        //init view
        with(binding) {
            llAds.setOnClickListener {
                DialogAds.build { }.show(requireFragmentManager(), tag(requireContext()))
            }
            tvTest.setOnClickListener {
                Intent(activity, SkinListActivity::class.java).let {
                    it.putExtra(SkinListActivity.IS_HIDE_SKINS, true)
                    startActivity(it)
                    requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            }

            if (!isDebug()) {
                tvTest.hide()
            }

            viewPager = rvCarousel
        }

        //init carousel
        carouselAdapter = CarouselAdapter(this, prefManager)
        with(viewPager) {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = carouselAdapter
            offscreenPageLimit = 3
        }

        //init heroes
        with(binding.rvHeroes) {
            isNestedScrollingEnabled = false
            heroesAdapter = CustomAdapter(this@HomeFragment, prefManager)
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
            adapter = heroesAdapter
        }
    }

    private fun setupView() {
        setupCarousel(requireContext(), viewPager, scrollHandler, scrollRunnable, SCROLL_DELAY)

        loadAdsActivityIntertisial(requireContext(), requireActivity(), adRequest, prefManager)

        //auto scrool carousel
        scrollHandler.postDelayed(scrollRunnable, SCROLL_DELAY)
    }

    private fun setList() {
        viewModel.getRequestMethod().observe(viewLifecycleOwner, requestMethod)
        viewModel.getAppStatus().observe(viewLifecycleOwner, appStatus)
        viewModel.getCarousel().observe(viewLifecycleOwner, carousel)
        viewModel.getAdsAdmob().observe(viewLifecycleOwner, adsAdmob)
        viewModel.getHeroes().observe(viewLifecycleOwner, heroes)
    }


    private val requestMethod = Observer<ApiResponse<List<RequestMethod>>> { data ->
        when (data) {
            is ApiResponse.Loading -> {
                observeLoading()
            }
            is ApiResponse.Success -> {
                prefManager.spSaveFirebaseMethod(data.data)

                observeSuccess()
            }
            is ApiResponse.Error -> {
                with(binding) {
                    hideShimmer(mShimmerViewContainer, mainContent)
                }
            }
            is ApiResponse.Empty -> {
            }
        }
    }

    private val adsAdmob = Observer<ApiResponse<List<Admob>>> { data ->
        if (data != null) {
            when (data) {
                is ApiResponse.Loading -> {
                    observeLoading()
                }
                is ApiResponse.Success -> {
                    with(binding) {
                        hideShimmer(mShimmerViewContainer, mainContent)
                    }
                    prefManager.spSaveAdsAdmob(data.data)

                    observeSuccess()
                }
                is ApiResponse.Error -> {
                    observeError(data.errorMessage)
                }

                else -> {
                }
            }
        }
    }

    private val appStatus = Observer<ApiResponse<List<AppStatus>>> { data ->
        if (data != null) {
            when (data) {
                is ApiResponse.Loading -> {
                    observeLoading()
                }
                is ApiResponse.Success -> {
                    with(binding) {
                        hideShimmer(mShimmerViewContainer, mainContent)
                    }

                    if (data.data.first().isServerDown!!) {
                        DialogServerError.build {}
                            .show(requireFragmentManager(), tag(requireContext()))
                    }

                    if (BuildConfig.VERSION_CODE < data.data.first().version_code!!) {
                        DialogUpdateApp.build {}
                            .show(requireFragmentManager(), tag(requireContext()))
                    }

                    observeSuccess()
                }
                is ApiResponse.Error -> {
                    observeError(data.errorMessage)
                }
                else -> {
                }
            }
        }
    }

    private val carousel = Observer<ApiResponse<List<Carousel>>> { data ->
        if (data != null) {
            when (data) {
                is ApiResponse.Loading -> {
                    observeLoading()
                }
                is ApiResponse.Success -> {
                    with(binding) {
                        hideShimmer(mShimmerViewContainer, mainContent)
                    }
                    setupDataListCarousel(data.data)

                    observeSuccess()
                }
                is ApiResponse.Error -> {
                    observeError(data.errorMessage)
                }
            }
        }
    }

    private val heroes = Observer<Resource<List<Heroes>>> { data ->
        if (data != null) {
            when (data) {
                is Resource.Loading -> {
                    observeLoading()
                }

                is Resource.Success -> {
                    if (data.data.isNullOrEmpty()) {
                        observeError(resources.getString(R.string.empty))
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

    private val endorse = Observer<Resource<List<Endorse>>> { data ->
        when (data) {
            is Resource.Loading -> {
                observeLoading()
            }
            is Resource.Success -> {
                prefManager.spCountEndorse += 1

                data.data?.map { data ->
                    if (data.name == "endorse_hero_list" && data.isEnable!!) {
                        if (dataListContent.isNotEmpty()) {
                            if (dataListContent.size > 1) {
                                if (data.isMultipleLoad!!) {
                                    clearEndorse(dataListContent, 0)
                                    clearEndorse(dataListContent, 2)
                                    dataListContent.add(0, data)
                                    dataListContent.add(2, data)
                                } else {
                                    clearEndorse(dataListContent, 0)
                                    dataListContent.add(0, data)
                                }
                            } else {
                                clearEndorse(dataListContent, 0)
                                dataListContent.add(0, data)
                            }
                        }
                        heroesAdapter.addList(dataListContent)
                    }
                }

                observeSuccess()
            }
            is Resource.Error -> {
                observeError(data.message.toString())
            }
        }
    }


    private fun setupDataListCarousel(data: List<Any>) {
        dataListCarousel.clear()
        dataListCarousel.addAll(data)
        carouselAdapter.addList(dataListCarousel)

        //setup ads native list
        val dataNativeAds = getIdNativeCarousel(prefManager)
        if (dataNativeAds.isEnable!!) {
            setupAdInRecycler(
                data.size,
                dataNativeAds,
                nativeAdListCarousel,
                requireContext(),
                dataListCarousel,
                adRequest,
                "carousel",
                1,
                requireActivity()
            ) {
                carouselAdapter.addList(dataListCarousel)
            }
        }
    }

    private fun setupDataList(data: List<Any>) {
        dataListContent.clear()
        dataListContent.addAll(data)
        heroesAdapter.addList(dataListContent)

        viewModel.getEndorse().observe(viewLifecycleOwner, endorse)

        //setup native ads
        val dataNativeAds = getIdNativeHeroList(prefManager)
        if (dataNativeAds.isEnable!!) {
            setupAdInRecycler(
                data.size,
                dataNativeAds,
                nativeAdListContent,
                requireContext(),
                dataListContent,
                adRequest,
                "heroes",
                2,
                requireActivity()
            ) {
                heroesAdapter.addList(dataListContent)
            }
        }
    }

    private fun setupRateView() {
        prefManager.spCountRate += 1
        if (!prefManager.spIsRate && prefManager.spCountRate % 10 == 0) {
            DialogUpdateApp.isViewRate = true
            DialogUpdateApp.build {
                DialogUpdateApp.isViewRate = false
            }.show(requireFragmentManager(), tag(requireContext()))
        }
    }


    private fun observeLoading() {
        with(binding) {
            showShimmer(mShimmerViewContainer, mainContent)
        }
    }

    private fun observeError(message: String) {
        with(binding) {
            showShimmer(mShimmerViewContainer, mainContent)
        }
        context?.toast(message)
    }

    private fun observeSuccess() {
        with(binding) {
            hideShimmer(mShimmerViewContainer, mainContent)
        }
    }


    override fun onEventClick(data: Carousel) {
        super.onEventClick(data)
        if (data.linkUrl != "") {
            AppAnalytics.trackClick(AppAnalytics.Const.CAROUSEL)
            intentToWeb(requireContext(), data.linkUrl.toString())
        } else if (data.activityUrl != "") {
            if (isAppInstalled(packageChrome, requireContext())) {
                intentCustomTabs(data.activityUrl!!, requireContext())
            } else {
                intentToWeb(requireContext(), data.activityUrl!!)
            }
        }
    }

    override fun onEventClick(data: Heroes) {
        super.onEventClick(data)
        AppAnalytics.trackClick(data.name)

        Intent(activity, SkinListActivity::class.java).let {
            it.putExtra(SkinListActivity.HEROES, data)
            startActivity(it)
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onEventClick(data: Skins) {
        super.onEventClick(data)
        AppAnalytics.trackClick(AppAnalytics.Const.POPULAR_SKINS)

        Intent(activity, DetailSkinActivity::class.java).let {
            it.putExtra(DetailSkinActivity.DETAIL_SKINS, data)
            startActivity(it)
            requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onEventClick(data: Endorse) {
        super.onEventClick(data)
        if (isAppInstalled(packageChrome, requireContext())) {
            intentCustomTabs(data.activityUrl!!, requireContext())
        } else {
            intentToWeb(requireContext(), data.activityUrl!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragment = null
    }
}