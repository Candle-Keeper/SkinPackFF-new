package id.candlekeeper.skinpackff.ui.dialog

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.callback.FileCallback
import com.anggrayudi.storage.callback.FolderCallback
import com.anggrayudi.storage.file.moveFolderTo
import com.google.android.gms.ads.AdRequest
import id.candlekeeper.core.databinding.ItemAdNativeListBinding
import id.candlekeeper.core.databinding.ItemAdNativeStartioBigBinding
import id.candlekeeper.core.databinding.ItemAdNativeStartioSmallBinding
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.utils.*
import id.candlekeeper.core.utils.function.*
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.databinding.FragmentDialogInstallSkinBinding
import id.candlekeeper.skinpackff.ui.detailSkin.DetailSkinActivity.Companion.SIZE_FILE
import id.candlekeeper.skinpackff.ui.detailSkin.DetailSkinViewModel
import id.candlekeeper.skinpackff.utils.function.Unzip
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class DialogInstallSkin(
    private val onSubmit: () -> Unit
) : BaseDialogFragment(), View.OnClickListener {

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var _fragmentBinding: FragmentDialogInstallSkinBinding? = null
    private val binding get() = _fragmentBinding as FragmentDialogInstallSkinBinding

    private var _adNativeBinding: ItemAdNativeListBinding? = null
    private val bindingNative get() = _adNativeBinding as ItemAdNativeListBinding

    private var _adNativeStartIoBig: ItemAdNativeStartioBigBinding? = null
    private val bindingNativeStartIoBig get() = _adNativeStartIoBig as ItemAdNativeStartioBigBinding

    private var _adNativeStartIoSmall: ItemAdNativeStartioSmallBinding? = null
    private val bindingNativeStartIoSmall get() = _adNativeStartIoSmall as ItemAdNativeStartioSmallBinding

    private val viewModel: DetailSkinViewModel by viewModel()
    private val prefManager: PrefManager by inject()
    private val adRequest: AdRequest by inject()

    private var buttonStatus = ""

    //skins
    private var detailSkins: Skins? = null

    val timerDummyInstall = object : CountUpTimer(300000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(second: Int) {
            with(binding) {
                tvSize.hide()
                progressInstall.show()
                progressInstall.progress = second
                when (second) {
                    30 -> tvDescription.text = "Extracting Skins.."
                    35 -> showAdNative(getIdNativeDialog(prefManager))
                    50 -> tvDescription.text = "Install Skins.."
                    65 -> showAdNative(getIdNativeDialog(prefManager))
                    70 -> tvDescription.text = "Extracting Skins.."
                    90 -> tvDescription.text = "Install Skins.."
                    100 -> {
                        tvDescription.text = "Extracting Skins.."
                        progressInstall.max = 200
                        progressInstall.progress = second
                    }
                    200 -> {
                        tvDescription.text = "Install Skins.."
                        progressInstall.max = 300
                        progressInstall.progress = second
                    }
                }
            }
        }
    }

    companion object {

        //parcelize
        const val SKINS = "skins"

        @JvmStatic
        fun build(detailSkins: Skins, onSubmit: () -> Unit) = DialogInstallSkin(onSubmit).apply {
            arguments = Bundle().apply {
                putParcelable(SKINS, detailSkins)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _fragmentBinding = FragmentDialogInstallSkinBinding.inflate(inflater, container, false)
        _adNativeBinding = ItemAdNativeListBinding.inflate(inflater, container, false)
        _adNativeStartIoBig = ItemAdNativeStartioBigBinding.inflate(inflater, container, false)
        _adNativeStartIoSmall = ItemAdNativeStartioSmallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViewCreated() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAdNative(getIdNativeDialog(prefManager))

        initView()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentBinding = null
        _adNativeBinding = null
        _adNativeStartIoBig = null
        _adNativeStartIoSmall = null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        ioScope.cancel()
        uiScope.cancel()
        timerDummyInstall.cancel()
        viewModel.compositeDisposable.dispose()
    }

    private fun initView() {
        with(binding) {
            btnInstall.setOnClickListener(this@DialogInstallSkin)
            btnCancel.setOnClickListener(this@DialogInstallSkin)
            tvProgress.text = "0 %"
        }

        //get skins from parcelize
        arguments?.getParcelable<Skins>(SKINS).let {
            detailSkins = it
        }

        isCancelable = false
        viewModel.startDownload(prefManager, requireContext(), detailSkins?.fileUrl.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        with(binding) {

            if (detailSkins?.size != "") {
                tvSize.text = resources.getString(R.string.size_file) + " ${detailSkins?.size}"
            } else {
                tvSize.text = resources.getString(R.string.size_file) + " $SIZE_FILE Mb"
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCancel -> {
                when (buttonStatus) {
                    "progress" -> {
                        AppAnalytics.trackDownload(AppAnalytics.Const.CANCEL)
                    }
                }
                dismiss()
                onSubmit()
            }

            R.id.btnInstall -> {
                when (buttonStatus) {
                    "progress" -> {
                        requireActivity().toast(resources.getString(R.string.tunggu_proses_download_belum_selesai))
                    }
                    "error" -> {
                        viewModel.startDownload(prefManager, requireContext(), detailSkins?.fileUrl.toString())
                    }
                    "success" -> {
                        doInstallFirst()
                    }
                    "install" -> {
                        requireActivity().toast(resources.getString(R.string.tunggu_proses_install_belum_selesai))
                    }
                    "success_install" -> {
                        intentToML()
                    }
                }
            }
        }
    }

    private fun doInstallFirst() {
        AppAnalytics.trackDownload(AppAnalytics.Const.INSTALL)
        DialogChooseMl {
            doInstallSkin()
        }.show(requireFragmentManager(), tag(requireContext()))
    }

    //proses install skins
    private fun doInstallSkin() {
        buttonStatus = "install"
        showAdNative(getIdNativeDialog(prefManager))
        val result = Unzip.unzip(
            destinationDownload(requireContext()),
            destinationUnzip(requireContext(), prefManager)
        )

        if (isAboveAndroid11()) {
            if (result) {
                val source = docFileOperation(1, activity?.application!!)!!
                val target = docFileOperation(2, activity?.application!!)!!
                Log.e("move", "source: ${source.uri} || target: ${target.uri}")
                ioScope.launch {
                    source.moveFolderTo(
                        activity?.application!!,
                        target,
                        false,
                        callback = createFolderCallback()
                    )
                }
            } else {
                installFail()
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                if (result) {
                    installSuccess()
                } else {
                    installFail()
                }
            }, 2000)
        }

        with(binding) {
            btnInstall.setOnClickListener { requireActivity().toast(resources.getString(R.string.tunggu_proses_install_belum_selesai)) }
            lootieProsesInstall(lootieDownload)
            tvDescription.text = resources.getString(R.string.tunggu_proses_install_sedang)
        }
    }

    //intent ke mobile legend
    private fun intentToML() {
        val launchIntent =
            requireActivity().packageManager.getLaunchIntentForPackage(prefManager.spPackageNameMl!!)

        if (launchIntent != null) {
            try {
                startActivity(launchIntent)
            } catch (e: ActivityNotFoundException) {
                Log.e(tag(requireContext()), e.message.toString())
            }
        } else {
            requireActivity().toast(resources.getString(R.string.ml_tidak_ada))
        }
    }

    //proses state download skin
    @SuppressLint("SetTextI18n")
    private fun observeData() {
        var indexProgress = 0
        viewModel.state.observe(viewLifecycleOwner) { result ->
            when (result) {

                //on start
                is DetailSkinViewModel.DownloadSkinState.Start -> {
                    buttonStatus = "progress"

                    if (isNotLoadAdsInters()) {
                        prefManager.spCountDownloadSkins++
                    }
                    if (prefManager.spCountDownloadSkins % (4..6).random() == 0) {
                        loadAdsActivityIntertisial(
                            requireContext(),
                            requireActivity(),
                            adRequest,
                            prefManager
                        )
                        prefManager.spCountDownloadSkins = 1
                    }

                    with(binding) {
                        lootieProsesDownload(lootieDownload)

                        btnInstall.text = resources.getString(R.string.install)
                    }
                }

                //on progress
                is DetailSkinViewModel.DownloadSkinState.Progress -> {
                    buttonStatus = "progress"
                    indexProgress += 1

                    // dinamic loop ads native
                    val adsNativeLoop = getIdNativeDialog(prefManager)
                    if (adsNativeLoop.countLoop != 0) {
                        if (indexProgress % adsNativeLoop.countLoop!! == 0) {
                            showAdNative(adsNativeLoop)
                        }
                    }

                    with(binding) {
                        tvDescription.text =
                            resources.getString(R.string.tunggu_proses_download_sedang_berlangsung)
                        tvProgress.text = result.progress.toString() + "%"

                        btnInstall.text = resources.getString(R.string.install)
                    }
                }

                //on error
                is DetailSkinViewModel.DownloadSkinState.Error -> {
                    buttonStatus = "error"

                    with(binding) {
                        lootieProsesFailed(lootieDownload)

                        tvDescription.text = result.message
                        tvProgress.text = ""

                        btnInstall.text = resources.getString(R.string.reload)
                    }

                    if (result.message == resources.getString(R.string.server_error)) {
//                        pushFailEvent("Fail Download")
                    }
                }

                //success
                is DetailSkinViewModel.DownloadSkinState.Succes -> {
                    buttonStatus = "success"

                    with(binding) {
                        lootieProsesSucces(lootieDownload)
                        tvProgress.text = ""
                        tvDescription.text = resources.getString(R.string.download_selesai)
                        btnInstall.text = resources.getString(R.string.install)
                    }
                }
                else -> {
                }
            }
        }
    }

    //setup native ads
    private fun showAdNative(dataNativeAds: Admob) {
        if (dataNativeAds.isEnable!!) {
            if (dataNativeAds.isEnable!!) {
                setupAdNativeBig(
                    dataNativeAds,
                    requireContext(),
                    requireActivity(),
                    requireActivity(),
                    this,
                    bindingNative.root,
                    binding.adsNativeBig.frameLayout,
                    bindingNativeStartIoBig,
                    adRequest,
                    null,
                    null
                )
            }

            val bannerAds = getIdBannerDownload(prefManager)
            if (bannerAds.isEnable!!) { //banner admob
//                showAdsBanner(
//                    requireContext(),
//                    requireActivity(),
//                    bannerAds,
//                    binding.adsNativeSmall.frameLayout,
//                    adRequest
//                )
            } else {
                setupAdsNativeApplovinSmall(
                    requireContext(),
                    bindingNativeStartIoSmall,
                    dataNativeAds, requireActivity(),
                    this,
                    binding.adsNativeSmall.frameLayout
                )
            }
        }
    }

    //callback install skins
    private fun createFolderCallback() = object : FolderCallback(uiScope) {

        override fun onStart(
            folder: DocumentFile,
            totalFilesToCopy: Int,
            workerThread: Thread
        ): Long {
            binding.progressInstall.max = 100

            timerDummyInstall.start()
            Log.e("move", "totalFilesToCopy: $totalFilesToCopy")
            return 1000 // update progress every 1 second
        }

        override fun onParentConflict(
            destinationFolder: DocumentFile,
            action: ParentFolderConflictAction,
            canMerge: Boolean
        ) {
            action.confirmResolution(ConflictResolution.values()[if (!canMerge) 2 else 1])
        }

        override fun onContentConflict(
            destinationFolder: DocumentFile,
            conflictedFiles: MutableList<FileConflict>,
            action: FolderContentConflictAction
        ) {
            Log.e("move", "onContentConflict: ${conflictedFiles.size}")

            val newSolution = ArrayList<FileConflict>(conflictedFiles.size)
            handleConflict(action, conflictedFiles, newSolution)
        }

        override fun onCompleted(result: Result) {
            super.onCompleted(result)
            Log.e("move", "result success")
            timerDummyInstall.cancel()
            job.cancel()
            ioScope.cancel()
            this@DialogInstallSkin.uiScope.cancel()
            installSuccess()
        }

        override fun onReport(report: Report) {
            super.onReport(report)
            Log.e(
                "move",
                "progress: ${report.progress} || copied: ${report.fileCount} || bytesMoved: ${report.bytesMoved} || writeSpeed: ${report.writeSpeed}"
            )
        }

        override fun onFailed(errorCode: ErrorCode) {
            Log.e("move", errorCode.name)
            timerDummyInstall.cancel()
            job.cancel()
            ioScope.cancel()
            this@DialogInstallSkin.uiScope.cancel()
            installFail()
        }
    }

    private fun installSuccess() {
        buttonStatus = "success_install"
        with(binding) {
            lootieProsesSucces(lootieDownload)
            progressInstall.hide()
            tvDescription.text = resources.getString(R.string.skin_berhasil_dipasang)
            btnInstall.text = resources.getString(R.string.buka_ml)
            btnCancel.text = resources.getString(R.string.kembali)
            btnInstall.setOnClickListener {
                intentToML()
            }
        }
    }

    private fun installFail() {
        with(binding) {
            progressInstall.hide()
            lootieProsesFailed(lootieDownload)
            tvDescription.text = resources.getString(R.string.coba_install_kembali)
            btnCancel.text = resources.getString(R.string.kembali)
            btnInstall.setOnClickListener {
                doInstallFirst()
            }
        }
    }

    private fun handleConflict(
        action: FolderCallback.FolderContentConflictAction,
        conflictedFiles: MutableList<FolderCallback.FileConflict>,
        newSolution: MutableList<FolderCallback.FileConflict>
    ) {
        val currentSolution = conflictedFiles.removeFirstOrNull()
        if (currentSolution == null) {
            action.confirmResolution(newSolution)
            return
        }
        currentSolution.solution = FileCallback.ConflictResolution.values()[0]
        newSolution.add(currentSolution)
        handleConflict(action, conflictedFiles, newSolution)
    }
}