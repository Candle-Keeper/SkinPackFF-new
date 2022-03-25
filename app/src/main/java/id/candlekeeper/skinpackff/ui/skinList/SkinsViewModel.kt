package id.candlekeeper.skinpackff.ui.skinList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.core.domain.model.dataContent.Skins
import id.candlekeeper.core.domain.model.dataIncome.Endorse

class SkinsViewModel(private val iAppUseCase: IAppUseCase) : ViewModel() {

    fun getSkins(idHero: Int): LiveData<Resource<List<Skins>>> {
        return iAppUseCase.getSkins(idHero).asLiveData()
    }

    fun getSkinsIsHide(): LiveData<ApiResponse<List<Skins>>> {
        return iAppUseCase.getSkinsIsHide().asLiveData()
    }

    fun searchSkins(idHero: Int, search: String): LiveData<List<Skins>> {
        return iAppUseCase.searchSkins(idHero, search).asLiveData()
    }

    fun searchAddSkins(idHero: Int, search: String): LiveData<List<Skins>> {
        return iAppUseCase.searchSkins(idHero, search).asLiveData()
    }

    fun getEndorseDb(): LiveData<List<Endorse>> {
        return iAppUseCase.getEndorseDb().asLiveData()
    }
}