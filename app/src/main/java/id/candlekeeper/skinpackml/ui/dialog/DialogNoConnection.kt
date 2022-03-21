package id.candlekeeper.skinpackml.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.candlekeeper.skinpackml.databinding.FragmentDialogNoConnectionBinding
import id.candlekeeper.core.utils.BaseDialogFragment


class DialogNoConnection(
    private val onSubmit: () -> Unit
) : BaseDialogFragment() {

    private var _fragmentBinding: FragmentDialogNoConnectionBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogNoConnectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogNoConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

        with(binding) {
            btnCancel.setOnClickListener {
                activity?.finish()
            }
            btnInstall.setOnClickListener {
                dismiss()
                onSubmit()
            }
            lootie.setAnimation("no_connection.json")
        }
        isCancelable = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }

    companion object {
        fun build(onSubmit: () -> Unit) = DialogNoConnection(onSubmit)
    }
}