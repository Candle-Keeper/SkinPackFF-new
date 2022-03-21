package id.candlekeeper.skinpackml.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import id.candlekeeper.core.databinding.ItemAdNativeStartioSmallBinding
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.*
import id.candlekeeper.skinpackml.databinding.FragmentDialogChooseMlBinding
import org.koin.android.ext.android.inject


class DialogChooseMl(
    private val onSubmit: (() -> Unit)
) : BaseDialogFragment() {

    private val adRequest: AdRequest by inject()
    private val prefManager: PrefManager by inject()

    private var _fragmentBinding: FragmentDialogChooseMlBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogChooseMlBinding

    private var _adNativeStartIoSmall: ItemAdNativeStartioSmallBinding? = null
    private val bindingNativeStartIoSmall get() = _adNativeStartIoSmall as ItemAdNativeStartioSmallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogChooseMlBinding.inflate(inflater, container, false)
        _adNativeStartIoSmall = ItemAdNativeStartioSmallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val dataBannerAds = getIdBannerDialog(prefManager)
            val dataNativeAds = getIdNativeDialog(prefManager)
            if (dataBannerAds.isEnable!!) { //banner admob
                showAdsBanner(
                    requireContext(),
                    requireActivity(),
                    dataBannerAds,
                    adsInclude.frameLayout,
                    adRequest
                )
            } else {
                setupAdsNativeApplovinSmall(
                    requireContext(),
                    bindingNativeStartIoSmall,
                    dataNativeAds,
                    requireActivity(),
                    this@DialogChooseMl,
                    adsInclude.frameLayout
                )
            }
        }

        initView()
    }

    private fun initView() {
        isCancelable = false

        with(binding) {
            btnVietnam.setOnClickListener {
                prefManager.spPackageNameMl = packageMlVietnam
                dismiss()
                onSubmit.invoke()
            }
            btnGlobal.setOnClickListener {
                prefManager.spPackageNameMl = packageMlGlobal
                dismiss()
                onSubmit.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
        _adNativeStartIoSmall = null
    }
}