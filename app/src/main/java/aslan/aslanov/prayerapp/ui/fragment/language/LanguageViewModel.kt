package aslan.aslanov.prayerapp.ui.fragment.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.model.language.LanguageResponse
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.QuranLanguageRepository
import kotlinx.coroutines.launch

class LanguageViewModel : ViewModel() {
    private val repository = QuranLanguageRepository()


    private var _languageResponse = MutableLiveData<LanguageResponse>()
    val languageResponse: LiveData<LanguageResponse>
        get() = _languageResponse

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun getLanguage() = viewModelScope.launch {
        repository.getQuranLanguage { result ->
            when (result.status) {
                Status.ERROR -> {
                    result.msg?.let {
                        _errorMessage.value=it
                        _loading.value=false
                    }
                }
                Status.LOADING -> {
                    _loading.value=true
                }
                Status.SUCCESS -> {
                    result.data?.let {
                        _languageResponse.value=it
                        _loading.value=false
                    }
                }
            }
        }
    }
}