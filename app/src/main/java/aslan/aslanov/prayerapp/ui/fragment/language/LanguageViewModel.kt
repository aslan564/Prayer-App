package aslan.aslanov.prayerapp.ui.fragment.language

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.language.LanguageResponse
import aslan.aslanov.prayerapp.model.language.category.HadithLanguageItem
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.HadithAndLanguageRepository
import kotlinx.coroutines.launch

class LanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val database= PrayerDatabase.getInstance(application)
    private val repository = HadithAndLanguageRepository(database)


    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun getLanguageSurah(onCompleteListener: (LanguageResponse)->Unit) = viewModelScope.launch {
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
                        onCompleteListener(it)
                        _loading.value=false
                    }
                }
            }
        }
    }
    fun getLanguageHadeeth(onCompleteListener: (List<HadithLanguageItem>)->Unit) = viewModelScope.launch {
        repository.getHadeethLanguage { result ->
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
                        onCompleteListener(it)
                        _loading.value=false
                    }
                }
            }
        }
    }

    fun getHadithCategory(onCompleteListener: (Boolean) -> Unit) =
        viewModelScope.launch {
            repository.addCategoryToDatabase()
            onCompleteListener(true)
        }
}