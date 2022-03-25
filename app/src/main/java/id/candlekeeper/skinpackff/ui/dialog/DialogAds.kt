package id.candlekeeper.skinpackff.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import id.candlekeeper.core.databinding.ItemAdNativeListBinding
import id.candlekeeper.core.databinding.ItemAdNativeStartioBigBinding
import id.candlekeeper.core.utils.BaseDialogFragment
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.core.utils.function.*
import id.candlekeeper.core.utils.hide
import id.candlekeeper.core.utils.show
import id.candlekeeper.skinpackff.databinding.FragmentDialogAdsBinding
import org.koin.android.ext.android.inject


class DialogAds(
    private val onSubmit: () -> Unit
) : BaseDialogFragment() {

    private val adRequest: AdRequest by inject()
    private val prefManager: PrefManager by inject()

    private var _fragmentBinding: FragmentDialogAdsBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogAdsBinding

    private var _adNativeStartIoBig: ItemAdNativeStartioBigBinding? = null
    private val bindingNativeStartIoBig get() = _adNativeStartIoBig as ItemAdNativeStartioBigBinding

    private var _adNativeBinding: ItemAdNativeListBinding? = null
    private val bindingNative get() = _adNativeBinding as ItemAdNativeListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogAdsBinding.inflate(inflater, container, false)
        _adNativeBinding = ItemAdNativeListBinding.inflate(inflater, container, false)
        _adNativeStartIoBig = ItemAdNativeStartioBigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        //setup native ads
        val dataNativeAds = getIdNativeDialog(prefManager)
        if (dataNativeAds.isEnable!!) {
            setupAdNativeBig(
                dataNativeAds,
                requireContext(),
                requireActivity(),
                requireActivity(),
                this,
                bindingNative.root,
                binding.adNative.frameLayout,
                bindingNativeStartIoBig,
                adRequest,
                binding.lottie,
                binding.btnBack
            )
        } else {
            binding.btnBack.show()
        }
    }

    private fun initView() {
        isCancelable = false

        with(binding) {
            lottie.setAnimation("celebration.json")
            btnBack.hide()

            btnBack.setOnClickListener {
                dismiss()
                onSubmit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
        _adNativeBinding = null
        _adNativeStartIoBig = null
    }

    companion object {
        fun build(onSubmit: () -> Unit) = DialogAds(onSubmit)
    }
}