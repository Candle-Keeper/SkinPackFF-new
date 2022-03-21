package id.candlekeeper.core.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<State> : ViewModel() {

    protected val TAG = this.javaClass.simpleName
    protected val _state = MutableLiveData<State>()

    val state: LiveData<State>
        get() = _state
}