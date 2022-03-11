package aslan.aslanov.prayerapp.model.baseViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {

    private var _data = MutableLiveData<T>()
    val data: LiveData<T>
        get() = _data


    private var _dataErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _dataErrorMessage

    private var _dataLoadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _dataLoadingState

    protected fun setData(incomeData: T?, onComplete: (Boolean) -> Unit) {
        onComplete(false)
        incomeData?.let {
            _data.value = it
            onComplete(true)
        }
    }

    protected fun setError(error: String) {
        _dataErrorMessage.value = error
    }

    protected fun setLoading(state: Boolean) {
        _dataLoadingState.value = state
    }

    protected fun <R> addSecondData(data: R, onComplete: (Boolean) -> Unit): MutableLiveData<R> {
        val newData = MutableLiveData(data)
        onComplete(true)
        return newData
    }

}