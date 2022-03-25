package id.candlekeeper.skinpackff.ui.detailSkin

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.core.domain.model.dataAdditional.UrlTutorial
import id.candlekeeper.core.utils.BaseViewModel
import id.candlekeeper.core.utils.PrefManager
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.utils.function.Download
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit

class DetailSkinViewModel(
    private val iAppUseCase: IAppUseCase
) : BaseViewModel<DetailSkinViewModel.DownloadSkinState>() {

    val compositeDisposable = CompositeDisposable()
    private val fileDownloader by lazy {
        Download(
            OkHttpClient.Builder()
                .build()
        )
    }

    sealed class DownloadSkinState {
        data class Start(val message: String) : DownloadSkinState()
        data class Progress(val progress: Int) : DownloadSkinState()
        data class Succes(val message: String) : DownloadSkinState()
        data class Error(val message: String) : DownloadSkinState()
    }

    fun startDownload(prefManager: PrefManager, context: Context, url: String) {
        _state.value = (DownloadSkinState.Start("start"))
        compositeDisposable.add(
            fileDownloader.download(context, prefManager.spBaseFiles + url)
                .throttleFirst(2, TimeUnit.SECONDS)
                .toFlowable(BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _state.value = (DownloadSkinState.Progress(it))
                    Log.d("progress", it.toString())
                }, {
                    _state.value = (DownloadSkinState.Error(it.message.toString()))
                    Log.d("throw", it.message.toString())
                }, {
                    _state.value =
                        (DownloadSkinState.Succes(context.resources.getString(R.string.complete_download)))
                    Log.d("succes", "Complete Downloaded..")
                })
        )
    }

    fun getUrlTutorial(): LiveData<ApiResponse<List<UrlTutorial>>> {
        return iAppUseCase.getUrlTutorial().asLiveData()
    }


    fun pushMonitor(dataMonitor: HashMap<String, RequestBody>): LiveData<ApiResponse<Boolean>> {
        return iAppUseCase.pushMonitor(dataMonitor).asLiveData()
    }

}