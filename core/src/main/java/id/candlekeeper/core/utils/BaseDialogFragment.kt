package id.candlekeeper.core.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.candlekeeper.core.R

abstract class BaseDialogFragment : DialogFragment() {
    abstract fun initViewCreated()

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            attributes.windowAnimations = R.style.DialogAnimation;
            setLayout(getBetterSize(), ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewCreated()
    }

    private fun getBetterSize(): Int {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context?.display?.getRealMetrics(displayMetrics)
        } else {
            val display = activity?.windowManager?.defaultDisplay
            display?.getMetrics(displayMetrics)
        }
        val width = displayMetrics.widthPixels
        val whiteSpaceSize = width / 10
        return width - whiteSpaceSize
    }
}