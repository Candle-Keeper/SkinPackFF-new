package id.candlekeeper.skinpackml.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.core.domain.model.dataBaseApp.AppStatus
import id.candlekeeper.core.domain.model.dataBaseApp.RequestMethod
import id.candlekeeper.core.domain.model.dataContent.Carousel
import id.candlekeeper.core.domain.model.dataContent.Category
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Admob
import id.candlekeeper.core.domain.model.dataIncome.Endorse

class HomeViewModel(private val iAppUseCase: IAppUseCase) : ViewModel() {

    fun getAppStatus(): LiveData<ApiResponse<List<AppStatus>>> {
        return iAppUseCase.getAppStatus().asLiveData()
    }

    fun getCarousel(): LiveData<ApiResponse<List<Carousel>>> {
        return iAppUseCase.getCarousel().asLiveData()
    }

    fun getCategory(): LiveData<Resource<List<Category>>> {
        return iAppUseCase.getCategory().asLiveData()
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

    fun getAddCategory(): LiveData<Resource<List<Category>>> {
        return iAppUseCase.getAddCategory().asLiveData()
    }

    fun getAllAddSkinsDb(): LiveData<List<Skins>> {
        return iAppUseCase.getAllAddSkinsDb().asLiveData()
    }
}