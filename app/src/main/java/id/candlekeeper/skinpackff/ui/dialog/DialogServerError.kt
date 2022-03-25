package id.candlekeeper.skinpackff.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.FragmentDialogServerErrorBinding
import id.candlekeeper.skinpackff.utils.*
import id.candlekeeper.core.utils.BaseDialogFragment
import id.candlekeeper.core.utils.lootieFixingServer


class DialogServerError(
    private val onSubmit: () -> Unit
) : BaseDialogFragment(), View.OnClickListener {

    private var _fragmentBinding: FragmentDialogServerErrorBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogServerErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogServerErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

        with(binding) {
            btnUpdate.setOnClickListener(this@DialogServerError)
            lootieFixingServer(lootie)
        }
        isCancelable = false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnUpdate -> {
                activity?.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }

    companion object {
        fun build(onSubmit: () -> Unit) = DialogServerError(onSubmit)
    }
}