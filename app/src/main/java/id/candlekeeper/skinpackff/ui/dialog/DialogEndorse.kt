package id.candlekeeper.skinpackff.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.utils.*
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.FragmentDialogEndorseBinding
import org.koin.android.ext.android.inject


class DialogEndorse(
    private val onSubmit: () -> Unit
) : BaseDialogFragment() {

    private val prefManager: PrefManager by inject()
    private var endorse: Endorse? = null

    private var _fragmentBinding: FragmentDialogEndorseBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogEndorseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogEndorseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        isCancelable = false

        //get endorse from parcelize
        arguments?.getParcelable<Endorse>(ENDORSE).let {
            endorse = it
        }

        with(binding) {
            if (endorse?.youtubeUrl != "") {
                ivBackground.setOnClickListener {
                    intentToYoutube(requireContext(), endorse?.youtubeUrl.toString())
                }
            } else {
                ivPlay.hide()
            }

            if (endorse?.packageApp != "") {
                btnInstall.text = resources.getString(R.string.install)
            } else {
                btnInstall.text = resources.getString(R.string.lihat)
            }

            if (endorse?.packageApp == "" && endorse?.webUrl == "" && endorse?.activityUrl == "") {
                btnInstall.hide()
            }

            btnInstall.setOnClickListener {
                if (endorse?.packageApp != "") {
                    AppAnalytics.trackClick(AppAnalytics.Const.ENDORSE_DIALOG_I)
                    intentToPlaystore(requireContext(), endorse?.packageApp.toString())
                    dismiss()
                } else {
                    btnInstall.text = resources.getString(R.string.lihat)
                    if (endorse?.webUrl != "") {
                        AppAnalytics.trackClick(AppAnalytics.Const.ENDORSE_DIALOG_C)
                        intentToWeb(requireContext(), endorse?.webUrl.toString())
                        dismiss()
                    } else {
                        if (isAppInstalled(packageChrome, requireContext())) {
                            intentCustomTabs(endorse!!.activityUrl!!, requireContext())
                        } else {
                            intentToWeb(requireContext(), endorse!!.activityUrl!!)
                        }
                    }
                }
            }

            btnLater.setOnClickListener {
                dismiss()
                onSubmit()
            }

            tvName.text = endorse?.title
            tvDescription.text = endorse?.description
            ivBackground.loadImageFull(prefManager, endorse?.imageUrl.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }

    companion object {
        const val ENDORSE = "ENDORSE"

        fun build(endorse: Endorse, onSubmit: () -> Unit) = DialogEndorse(onSubmit).apply {
            arguments = Bundle().apply {
                putParcelable(ENDORSE, endorse)
            }
        }
    }
}