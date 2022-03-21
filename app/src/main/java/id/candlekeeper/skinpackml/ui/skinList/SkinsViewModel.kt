package id.candlekeeper.skinpackml.ui.skinList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.data.source.remote.network.ApiResponse
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.domain.model.dataContent.Skins
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest

class SkinsViewModel(private val iAppUseCase: IAppUseCase) : ViewModel() {

    fun getSkins(idHero: Int): LiveData<Resource<List<Skins>>> {
        return iAppUseCase.getSkins(idHero).asLiveData()
    }

    fun getSkinsIsHide(): LiveData<ApiResponse<List<Skins>>> {
        return iAppUseCase.getSkinsIsHide().asLiveData()
    }

    fun getAddSkins(idHero: Int): LiveData<Resource<List<Skins>>> {
        return iAppUseCase.getAddSkins(idHero).asLiveData()
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