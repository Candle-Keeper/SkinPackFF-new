package id.candlekeeper.skinpackml.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.core.domain.model.dataBaseApp.BaseApp

class SplashViewModel(private val iAppUseCase: IAppUseCase) : ViewModel() {

    fun getDinToken(): LiveData<ApiResponse<BaseApp>> {
        return iAppUseCase.getDinToken().asLiveData()
    }
}