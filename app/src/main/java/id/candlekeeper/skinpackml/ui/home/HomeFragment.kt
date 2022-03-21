package id.candlekeeper.skinpackml.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import id.candlekeeper.core.adapter.CategoryAdapter
import id.candlekeeper.core.adapter.CustomAdapter
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.model.dataBaseApp.AppStatus
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Category
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.getIdNativeCarousel
import id.candlekeeper.core.utils.function.loadAdsActivityIntertisial
import id.candlekeeper.core.utils.function.setupAdInRecycler
import id.candlekeeper.skinpackml.BuildConfig
import id.candlekeeper.skinpackml.R
import id.candlekeeper.skinpackml.databinding.FragmentHomeBinding
import id.candlekeeper.skinpackml.ui.detailSkin.DetailSkinActivity
import id.candlekeeper.skinpackml.ui.dialog.DialogAds
import id.candlekeeper.skinpackml.ui.dialog.DialogEndorse
import id.candlekeeper.skinpackml.ui.dialog.DialogServerError
import id.candlekeeper.skinpackml.ui.dialog.DialogUpdateApp
import id.candlekeeper.skinpackml.ui.heroList.HeroListActivity
import id.candlekeeper.skinpackml.ui.skinList.SkinListActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), OnItemClicked {

    private var _homeFragment: FragmentHomeBinding? = null
    private val binding get() = _homeFragment as FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModel()
    private val prefManager: PrefManager by inject()

    //Native Add
    private val adRequest: AdRequest by inject()
    private var dataList = mutableListOf<Any>()
    private val nativeAdList = mutableListOf<NativeAd>()

    //data addCategory and endrose
    private var dataListAdditonal = mutableListOf<Any>()

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
    private lateinit var skinsAdapter: CustomAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var addCategoryAdapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _homeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        //init popular
        with(binding.rvSkins) {
            isNestedScrollingEnabled = false
            skinsAdapter = CustomAdapter(this@HomeFragment, prefManager)
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = skinsAdapter
        }

        //init add category
        with(binding.rvAddCategory) {
            isNestedScrollingEnabled = false
            addCategoryAdapter = CustomAdapter(this@HomeFragment, prefManager)
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
            adapter = addCategoryAdapter
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
        viewModel.getCategory().observe(viewLifecycleOwner, category)
        viewModel.getAppStatus().observe(viewLifecycleOwner, appStatus)
        viewModel.getCarousel().observe(viewLifecycleOwner, carousel)
        viewModel.getAdsAdmob().observe(viewLifecycleOwner, adsAdmob)
        viewModel.getAddCategory().observe(viewLifecycleOwner, addCategory)
        viewModel.getAllAddSkinsDb().observe(viewLifecycleOwner, allAddSkinsDb)
    }

    private val requestMethod = Observer<ApiResponse<List<RequestMethod>>> { data ->
        when (data) {
            is ApiResponse.Loading -> {
            }
            is ApiResponse.Success -> {
                prefManager.spSaveFirebaseMethod(data.data)
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
                }
                is ApiResponse.Error -> {
                    observeError(data.errorMessage)
                }
            }
        }
    }

    private val category = Observer<Resource<List<Category>>> { data ->
        if (data != null) {
            when (data) {
                is Resource.Loading -> {
                    observeLoading()
                }
                is Resource.Success -> {

                    //init category
                    categoryAdapter = CategoryAdapter(this, prefManager)
                    with(binding.rvCategory) {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(
                            requireContext(),
                            1,
                            GridLayoutManager.HORIZONTAL,
                            false
                        )
                        adapter = categoryAdapter
                    }

                    with(binding) {
                        hideShimmer(mShimmerViewContainer, mainContent)
                    }
                    categoryAdapter.addList(data.data!!)
                }
                is Resource.Error -> {
                    observeError(data.message.toString())
                }
            }
        }
    }

    private val allAddSkinsDb = Observer<List<Skins>> { data ->
        if (data.isNotEmpty()) {
            skinsAdapter.addList(data.shuffled().take(10))
        } else {
            with(binding) {
                rvSkins.hide()
                textView3.hide()
            }
        }
    }

    private val addCategory = Observer<Resource<List<Category>>> { data ->
        if (data != null) {
            when (data) {
                is Resource.Loading -> {
                    observeLoading()
                }
                is Resource.Success -> {
                    with(binding) {
                        hideShimmer(mShimmerViewContainer, mainContent)
                    }
                    dataListAdditonal.clear()
                    if (dataListAdditonal.addAll(data.data!!)) {
                        viewModel.getEndorse().observe(viewLifecycleOwner, endorse)
                    }
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
            }
            is Resource.Success -> {
                prefManager.spCountEndorse += 1

                with(binding) {
                    hideShimmer(mShimmerViewContainer, mainContent)
                    data.data?.map { data ->
                        when (data.name) {
                            "endorse_home1" -> {
                                if (data.isEnable!!) {
                                    if (dataListAdditonal[1] is Endorse) {
                                        dataListAdditonal.removeAt(1)
                                    }
                                    dataListAdditonal.add(1, data)
                                    addCategoryAdapter.addList(dataListAdditonal)
                                }
                            }
                            "endorse_home2" -> {
                                if (data.isEnable!!) {
                                    if (dataListAdditonal.size >= 4) {
                                        if (dataListAdditonal[3] is Endorse) {
                                            dataListAdditonal.removeAt(3)
                                        }
                                    }
                                    dataListAdditonal.add(3, data)
                                    addCategoryAdapter.addList(dataListAdditonal)
                                }
                            }
                            "endorse_dialog" -> {
                                if (data.isEnable!! && prefManager.spCountEndorse % data.showingIndex!! == 0) {
                                    DialogEndorse.build(data) {}
                                        .show(requireFragmentManager(), tag(requireContext()))
                                }
                            }
                        }
                    }
                }
            }
            is Resource.Error -> {
                observeError(data.message.toString())
            }
        }
    }

    private fun setupDataListCarousel(data: List<Any>) {
        dataList.clear()
        dataList.addAll(data)
        carouselAdapter.addList(dataList)

        //setup ads native list
        val dataNativeAds = getIdNativeCarousel(prefManager)
        if (dataNativeAds.isEnable!!) {
            setupAdInRecycler(
                data.size,
                dataNativeAds,
                nativeAdList,
                requireContext(),
                dataList,
                adRequest,
                "carousel",
                1,
                requireActivity()
            ) {
                carouselAdapter.addList(dataList)
            }
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

    private fun setupRateView() {
        prefManager.spCountRate += 1
        if (!prefManager.spIsRate && prefManager.spCountRate % 10 == 0) {
            DialogUpdateApp.isViewRate = true
            DialogUpdateApp.build {
                DialogUpdateApp.isViewRate = false
            }.show(requireFragmentManager(), tag(requireContext()))
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

    override fun onEventClick(data: Category) {
        super.onEventClick(data)
        AppAnalytics.trackClick(data.name)

        Intent(activity, HeroListActivity::class.java).let {
            it.putExtra(HeroListActivity.CATEGORY, data)
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