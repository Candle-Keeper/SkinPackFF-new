package id.candlekeeper.skinpackff.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.core.utils.toast
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.ActivityHomeBinding
import org.koin.android.ext.android.inject


class HomeActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val prefManager: PrefManager by inject()

    private val classNameMore: String
        get() = "id.candlekeeper.more.MoreFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        prefManager.spIsSAF = false
        prefManager.spCountDownloadSkins = 1

        initView()
    }

    private fun initView() {
        navigationChange(HomeFragment())

        //test mediation ads
//        MediationTestSuiteite.launch(this)

        binding.bottomNavigationContainer.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_skins -> {
                    navigationChange(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.menu_more -> {
                    moreFragment()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun navigationChange(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun instantiateFragment(className: String): Fragment? {
        return try {
            Class.forName(className).newInstance() as Fragment
        } catch (e: Exception) {
            Log.e("aaa", e.message.toString())
            toast("Feature not found for your Android")
            null
        }
    }

    private fun moreFragment() {
        val fragment = instantiateFragment(classNameMore)
        if (fragment != null) {
            navigationChange(fragment)
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        toast(resources.getString(R.string.please_click))
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    fun updateStatusBarColor(color: String?) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }
}