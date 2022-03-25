package id.candlekeeper.skinpackff.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.core.domain.model.dataBaseApp.AppStatus
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Heroes
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.domain.model.dataIncome.Endorse

class HomeViewModel(private val iAppUseCase: IAppUseCase) : ViewModel() {

    fun getAppStatus(): LiveData<ApiResponse<List<AppStatus>>> {
        return iAppUseCase.getAppStatus().asLiveData()
    }

    fun getCarousel(): LiveData<ApiResponse<List<Carousel>>> {
        return iAppUseCase.getCarousel().asLiveData()
    }

    fun getAdsAdmob(): LiveData<ApiResponse<List<Admob>>> {
        return iAppUseCase.getAdsAdmob().asLiveData()
    }

    fun getEndorse(): LiveData<Resource<List<Endorse>>> {
        return iAppUseCase.getEndorse().asLiveData()
    }

    fun getRequestMethod(): LiveData<ApiResponse<List<RequestMethod>>> {
        return iAppUseCase.getRequestMethod().asLiveData()
    }

    fun getHeroes(): LiveData<Resource<List<Heroes>>> {
        return iAppUseCase.getHeroes().asLiveData()
    }
}