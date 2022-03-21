package id.candlekeeper.core.utils

import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.shimmer.ShimmerFrameLayout
import id.candlekeeper.core.BuildConfig
import id.candlekeeper.core.R
import com.bumptech.glide.load.DecodeFormat

import com.bumptech.glide.request.RequestOptions


//lootie proeses download
fun lootieProsesDownload(view: LottieAnimationView) {
    with(view) {
        setAnimation("download.json")
        playAnimation()
        loop(true)
    }
}

//lootie proses install
fun lootieProsesInstall(view: LottieAnimationView) {
    with(view) {
        setAnimation("install.json")
        playAnimation()
        loop(true)
    }
}

//lootie succes
fun lootieProsesSucces(view: LottieAnimationView) {
    with(view) {
        setAnimation("success.json")
        playAnimation()
        repeatCount = 3
    }
}

//lootie failed
fun lootieProsesFailed(view: LottieAnimationView) {
    with(view) {
        setAnimation("failed.json")
        playAnimation()
        repeatCount = 3
    }
}

//lootie connection
fun lootieFixingServer(view: LottieAnimationView) {
    with(view) {
        setAnimation("fixing_server.json")
        playAnimation()
        repeatCount = 3
    }
}

//lootie empty
fun lootieEmpty(view: LottieAnimationView) {
    with(view) {
        setAnimation("empty.json")
        playAnimation()
        repeatCount = 3
    }
}

//show shimmer
fun showShimmer(view: ShimmerFrameLayout, hide: View) {
    with(view) {
        show()
        startShimmer()
    }
    with(hide) {
        hide()
    }
}

//hide shimmer
fun hideShimmer(view: ShimmerFrameLayout, show: View) {
    with(view) {
        hide()
        stopShimmer()
    }
    with(show) {
        show()
    }
}

//setup view carousel
fun setupCarousel(
    contex: Context,
    viewPager: ViewPager2,
    scrollHandler: Handler,
    scrollRunnable: Runnable,
    SCROLL_DELAY: Long
) {
    val compositePageTransformer = CompositePageTransformer()
    val pageMargin = contex.resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
    val pageOffset = contex.resources.getDimensionPixelOffset(R.dimen.offset).toFloat()

    compositePageTransformer.addTransformer { page, position ->
        val myOffset: Float = position * -(2 * pageOffset + pageMargin)
        val r = 1 - kotlin.math.abs(position)
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -myOffset
            } else {
                page.translationX = myOffset
            }
        } else {
            page.translationY = myOffset
        }
        page.scaleY = 0.85f + r * 0.15f
    }

    viewPager.apply {
        setPageTransformer(compositePageTransformer)
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollHandler.removeCallbacks(scrollRunnable)
                scrollHandler.postDelayed(scrollRunnable, SCROLL_DELAY)
            }
        })
    }
}

//load image
fun ImageView.loadImage(prefManager: PrefManager, url: String) {
    Glide.with(this)
        .load(prefManager.spBaseFiles + url)
        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.ic_broken_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .override(150, 100)
        .thumbnail(0.25f)
        .into(this)
}

//load image
fun ImageView.loadImageFull(prefManager: PrefManager, url: String) {
    Glide.with(this)
        .load(prefManager.spBaseFiles + url)
        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.ic_broken_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .thumbnail(0.25f)
        .into(this)
}

//load image youtube
fun ImageView.loadImageYt(url: String) {
    val ytId = extractYoutubeVideoId(url)
    val load = tumbnailYt(ytId.toString())
    Glide.with(this)
        .load(load)
        .placeholder(R.drawable.loading_anim)
        .error(R.drawable.ic_broken_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .thumbnail(0.25f)
        .centerCrop()
        .into(this)
}

//load image resource
fun ImageView.loadImageRs(context: Context, assetName: String) {
    Glide.with(this)
        .load(resources.getIdentifier(assetName, "drawable", context.packageName))
        .centerCrop()
        .into(this)
}

//laod image
fun ImageView.loadColor(context: Context, color: Int) {
    Glide.with(this)
        .load("")
        .placeholder(ColorDrawable(ContextCompat.getColor(context, color)))
        .into(this)
}

fun intentCustomTabs(url: String, context: Context) {
    var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    var mClient: CustomTabsClient? = null
    var mCustomTabsSession: CustomTabsSession? = null

    mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(
            componentName: ComponentName,
            customTabsClient: CustomTabsClient
        ) {
            //Pre-warming
            mClient = customTabsClient
            mClient?.warmup(0L)
            mCustomTabsSession = mClient?.newSession(null)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mClient = null
        }
    }

    CustomTabsClient.bindCustomTabsService(
        context, packageChrome,
        mCustomTabsServiceConnection as CustomTabsServiceConnection
    )

    val customTabsIntent = CustomTabsIntent.Builder(mCustomTabsSession)
        .setShowTitle(true)
//        .setUrlBarHidingEnabled(true)
        .build()


    customTabsIntent.launchUrl(context, Uri.parse(url))
}