package id.candlekeeper.more.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.candlekeeper.core.utils.BaseDialogFragment
import id.candlekeeper.more.databinding.FragmentDialogAboutBinding


class DialogAbout(
    private val onSubmit: () -> Unit
) : BaseDialogFragment() {

    private var _fragmentBinding: FragmentDialogAboutBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        isCancelable = false

        with(binding) {
            btnCancel.setOnClickListener {
                dismiss()
                onSubmit()
            }
        }
    }

    companion object {
        fun build(onSubmit: () -> Unit) = DialogAbout(onSubmit)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
    }
}