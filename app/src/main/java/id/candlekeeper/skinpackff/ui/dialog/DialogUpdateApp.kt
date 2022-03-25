package id.candlekeeper.skinpackff.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import id.candlekeeper.core.databinding.ItemAdNativeStartioSmallBinding
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.*
import id.candlekeeper.skinpackff.BuildConfig
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.FragmentDialogUpdateAppBinding
import org.koin.android.ext.android.inject

class DialogUpdateApp(
    private val onSubmit: () -> Unit
) : BaseDialogFragment() {

    private val prefManager: PrefManager by inject()

    private var _fragmentBinding: FragmentDialogUpdateAppBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogUpdateAppBinding

    private var _adNativeStartIoSmall: ItemAdNativeStartioSmallBinding? = null
    private val bindingNativeStartIoSmall get() = _adNativeStartIoSmall as ItemAdNativeStartioSmallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogUpdateAppBinding.inflate(inflater, container, false)
        _adNativeStartIoSmall = ItemAdNativeStartioSmallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onResume() {
        super.onResume()

        with(binding) {
            val dataBannerAds = getIdBannerDialog(prefManager)
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
                    this@DialogUpdateApp,
                    adsInclude.frameLayout
                )
            }
        }
    }

    private fun initView() {

        isCancelable = false

        if (isViewRate) {
            with(binding) {
                lootie.setAnimation("rate.json")
                tvSize.hide()
                tvDescription.text = resources.getString(R.string.rate)
                btnUpdate.text = "OK"
            }
        } else {
            with(binding) {
                lootie.setAnimation("update.json")
                tvDescription.text =
                    resources.getString(R.string.update_untuk_mendapatkan_fitur_terbaru)
                btnUpdate.text = "Update"
            }
        }

        with(binding) {
            btnUpdate.setOnClickListener {
                if (isViewRate) {
                    prefManager.spIsRate = true
                    dismiss()
                }
                intentToPlaystore(requireContext(), BuildConfig.APPLICATION_ID)
            }
            btnCancel.setOnClickListener {
                dismiss()
                onSubmit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
        _adNativeStartIoSmall = null
    }

    companion object {
        fun build(onSubmit: () -> Unit) = DialogUpdateApp(onSubmit)
        var isViewRate = false
    }
}