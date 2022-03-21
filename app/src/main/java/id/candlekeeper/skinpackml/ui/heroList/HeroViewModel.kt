package id.candlekeeper.skinpackml.ui.heroList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.candlekeeper.core.data.Resource
import id.candlekeeper.core.domain.IAppUseCase
import id.candlekeeper.core.domain.model.dataIncome.Endorse
import id.candlekeeper.core.domain.model.dataContent.Heroes

class HeroViewModel(private val iAppUseCase: IAppUseCase) : ViewModel() {

    fun getHeroes(idCategory: Int): LiveData<Resource<List<Heroes>>> {
        return iAppUseCase.getHeroes(idCategory).asLiveData()
    }

    fun getAddHeroes(idCategory: Int): LiveData<Resource<List<Heroes>>> {
        return iAppUseCase.getAddHeroes(idCategory).asLiveData()
    }

    fun searchHeroes(idCategory: Int, search: String): LiveData<List<Heroes>> {
        return iAppUseCase.searchHeroes(idCategory, search).asLiveData()
    }

    fun searchAddHeroes(idCategory: Int, search: String): LiveData<List<Heroes>> {
        return iAppUseCase.searchAddHeroes(idCategory, search).asLiveData()
    }

    fun getEndorseDb(): LiveData<List<Endorse>> {
        return iAppUseCase.getEndorseDb().asLiveData()
    }
}