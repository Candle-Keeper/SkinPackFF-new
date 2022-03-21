package id.candlekeeper.skinpackml.ui.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import id.candlekeeper.core.BuildConfig
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.databinding.ItemAdNativeStartioSmallBinding
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.*
import id.candlekeeper.skinpackml.databinding.FragmentDialogFeedbackBinding
import id.candlekeeper.skinpackml.ui.detailSkin.DetailSkinViewModel
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class DialogFeedback(
    private val onSubmit: () -> Unit,
) : BaseDialogFragment() {

    private val adRequest: AdRequest by inject()
    private val prefManager: PrefManager by inject()
    private val viewModel: DetailSkinViewModel by viewModel()

    private var _fragmentBinding: FragmentDialogFeedbackBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogFeedbackBinding

    private var _adNativeStartIoSmall: ItemAdNativeStartioSmallBinding? = null
    private val bindingNativeStartIoSmall get() = _adNativeStartIoSmall as ItemAdNativeStartioSmallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _fragmentBinding = FragmentDialogFeedbackBinding.inflate(inflater, container, false)
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
                    this@DialogFeedback,
                    adsInclude.frameLayout
                )
            }
        }

        initView()
    }

    private fun initView() {
        isCancelable = false
        loadAdsAllDialogIntertisial(requireContext(), requireActivity(), adRequest, prefManager)

        when (fromPage) {
            "detail" -> {
                with(binding) {
                    etName.hide()
                    submitIssue()
                }
            }

            "more" -> {
                submitFeedback()
            }
        }

        //load ads when text count 2
        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start == 2) {
                    loadAdsAllDialogIntertisial(
                        requireContext(),
                        requireActivity(),
                        adRequest,
                        prefManager
                    )
                }
            }
        })

        binding.lootie.setAnimation("feedback.json")

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun submitFeedback() {
        val mapMonitor = HashMap<String, RequestBody>()

        with(binding) {
            btnSubmit.setOnClickListener {
                listOf<View>(
                    tlMessage, tLName
                ).clearErrorInputLayout()

                if (etMessage.text.isNullOrEmpty()) {
                    tlMessage.error = "tidak boleh kosong"
                    return@setOnClickListener
                }

                if (etName.text.isNullOrEmpty()) {
                    tLName.error = "tidak boleh kosong"
                    return@setOnClickListener
                }

                mapMonitor["type"] = "Feedback".toMultipartForm()
                mapMonitor["device"] = getDeviceName().toMultipartForm()
                mapMonitor["script_url"] = "".toMultipartForm()
                mapMonitor["script_name"] = "".toMultipartForm()
                mapMonitor["message"] = etMessage.text.toString().toMultipartForm()
                mapMonitor["sender"] = etName.text.toString().toMultipartForm()

                viewModel.pushMonitor(mapMonitor).observe(viewLifecycleOwner, submitMonitor)
            }
        }
    }

    private fun submitIssue() {
        val mapMonitor = HashMap<String, RequestBody>()

        with(binding) {
            btnSubmit.setOnClickListener {
                listOf<View>(
                    tlMessage
                ).clearErrorInputLayout()

                if (etMessage.text.isNullOrEmpty()) {
                    tlMessage.error = "tidak boleh kosong"
                    return@setOnClickListener
                }

                mapMonitor["type"] = "Issue".toMultipartForm()
                mapMonitor["device"] = getDeviceName().toMultipartForm()
                mapMonitor["script_url"] = (prefManager.spBaseFiles + fileUrl).toMultipartForm()
                mapMonitor["script_name"] = nameSkins.toMultipartForm()
                mapMonitor["message"] = etMessage.text.toString().toMultipartForm()
                mapMonitor["sender"] = "Admin".toMultipartForm()

                viewModel.pushMonitor(mapMonitor).observe(viewLifecycleOwner, submitMonitor)
            }
        }
    }

    private val submitMonitor = Observer<ApiResponse<Boolean>> { data ->
        if (data != null) {
            when (data) {
                is ApiResponse.Loading -> {
                    with(binding) {
                        progress.show()
                        btnSubmit.hide()
                    }
                }
                is ApiResponse.Success -> {
                    with(binding) {
                        progress.hide()
                        context?.toast("success, thanks you..")
                        dismiss()
                        onSubmit()
                    }
                }
                is ApiResponse.Error -> {
                    context?.toast(data.errorMessage)
                    binding.progress.hide()
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
        _adNativeStartIoSmall = null
    }

    companion object {
        fun build(onSubmit: () -> Unit) = DialogFeedback(onSubmit)
        var fromPage = ""
        var fileUrl = ""
        var nameSkins = ""
    }
}