package id.candlekeeper.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import id.candlekeeper.core.databinding.ItemAdNativeStartioSmallBinding
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.*
import id.candlekeeper.more.databinding.FragmentMoreBinding
import id.candlekeeper.more.di.moreModule
import id.candlekeeper.more.dialog.DialogAbout
import id.candlekeeper.skinpackff.ui.dialog.DialogFeedback
import id.candlekeeper.skinpackff.ui.home.HomeActivity
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import java.util.*

@Keep
class MoreFragment : Fragment() {

    // Ads
    private val adRequest: AdRequest by inject()

    private val prefManager: PrefManager by inject()

    private var _fragmentMore: FragmentMoreBinding? = null
    private val binding get() = _fragmentMore as FragmentMoreBinding

    private var _adNativeStartIoSmall: ItemAdNativeStartioSmallBinding? = null
    private val bindingNativeStartIoSmall get() = _adNativeStartIoSmall as ItemAdNativeStartioSmallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _fragmentMore = FragmentMoreBinding.inflate(inflater, container, false)
        _adNativeStartIoSmall = ItemAdNativeStartioSmallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onResume() {
        super.onResume()

        setupView()
    }

    private fun initView() {

        loadKoinModules(moreModule)

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

        binding.lottieAnimationView.setAnimation("approve.json")
        showGreetings()
    }

    private fun setupView() {

        with(binding) {
            val dataBannerAds = getIdBannerMore(prefManager)
            val dataNativeAds = getIdNativeDialog(prefManager)
            if (dataBannerAds.isEnable!!) { //banner admob
//                showAdsBanner(
//                    requireContext(),
//                    requireActivity(),
//                    dataBannerAds,
//                    adsInclude.frameLayout,
//                    adRequest
//                )
            } else {
                setupAdsNativeApplovinSmall(
                    requireContext(),
                    bindingNativeStartIoSmall,
                    dataNativeAds,
                    requireActivity(),
                    null,
                    adsInclude.frameLayout
                )
            }

            llChangeLanguage.setOnClickListener {
                AppAnalytics.trackClick(AppAnalytics.Const.CHANGE_LANG)
                changeLanguage()
            }

            llChangeTheme.setOnClickListener {
                AppAnalytics.trackClick(AppAnalytics.Const.CHANGE_THEME)
                chooseThemeDialog()
            }

            llShareApp.setOnClickListener {
                AppAnalytics.trackClick(AppAnalytics.Const.SHARE_APP)
                shareApp()
            }

            llRateApp.setOnClickListener {
                AppAnalytics.trackClick(AppAnalytics.Const.GIVE_RATING)
                intentToPlaystore(requireContext(), BuildConfig.APPLICATION_ID)
            }

            llFeedback.setOnClickListener {
                DialogFeedback.fromPage = "more"
                AppAnalytics.trackClick(AppAnalytics.Const.ADD_FEEDBACK)
                DialogFeedback.build() {
                    //show dinamic ads interstisial
                    showAdsAllDialogIntertisial(requireContext(), requireActivity(), prefManager)
                }.show(requireFragmentManager(), tag(requireContext()))
            }

            llAbout.setOnClickListener {
                DialogAbout.build() {}.show(requireFragmentManager(), tag(requireContext()))
            }

//            llDonate.setOnClickListener {
//                DialogDonate.build() {}.show(requireFragmentManager(), tag(requireContext()))
//            }
        }
    }

    private fun changeLanguage() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("more", e.message.toString())
        }
    }

    //intent to share app
    private fun shareApp() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, packagePlaystore2 + BuildConfig.APPLICATION_ID)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun chooseThemeDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("chose Theme")
        val styles = arrayOf("Light", "Dark", "System default")
        val checkedItem = prefManager.spTheme

        builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

            when (which) {
                0 -> {
                    try {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        prefManager.spTheme = 0
                        (activity as AppCompatActivity?)!!.delegate.applyDayNight()
                        dialog.dismiss()
                    } catch (e: Exception) {
                        context?.toast("not available on your android")
                    }
                }
                1 -> {
                    try {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        prefManager.spTheme = 1
                        (activity as AppCompatActivity?)!!.delegate.applyDayNight()
                        dialog.dismiss()
                    } catch (e: Exception) {
                        context?.toast("not available on your android")
                    }
                }
                2 -> {
                    try {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        prefManager.spTheme = 2
                        (activity as AppCompatActivity?)!!.delegate.applyDayNight()
                        dialog.dismiss()
                    } catch (e: Exception) {
                        context?.toast("not available on your android")
                    }
                }

            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showGreetings() {
        val c: Calendar = Calendar.getInstance()
        when (c.get(Calendar.HOUR_OF_DAY)) {
            in 4..9 -> {
                binding.tvGreetings.text = getString(R.string.pagi)
            }
            in 10..13 -> {
                binding.tvGreetings.text = getString(R.string.siang)
            }
            in 14..17 -> {
                binding.tvGreetings.text = getString(R.string.sore)
            }
            in 18..24 -> {
                binding.tvGreetings.text = getString(R.string.malam)
            }
            else -> {
                binding.tvGreetings.text = getString(R.string.malam)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentMore = null
        _adNativeStartIoSmall = null
    }
}