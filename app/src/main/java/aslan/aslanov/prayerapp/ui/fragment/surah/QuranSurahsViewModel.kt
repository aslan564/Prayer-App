package aslan.aslanov.prayerapp.ui.fragment.surah

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.model.surahs.Data
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.QuranSurahRepository
import aslan.aslanov.prayerapp.util.logApp
import kotlinx.coroutines.launch

class QuranSurahsViewModel : ViewModel() {
    private val repository = QuranSurahRepository()

    private var _quranResponse = MutableLiveData<List<Data>>()
    val quranResponse: LiveData<List<Data>>
        get() = _quranResponse

    private var _quranUiState = MutableLiveData<Boolean>()
    val quranUiState: LiveData<Boolean>
        get() = _quranUiState

    fun fetchQuran(quranType: String) = viewModelScope.launch {
        repository.fetchQuran { res ->
            when (res.status) {
                Status.LOADING -> {
                    _quranUiState.value=true
                }
                Status.ERROR -> {
                    res.msg?.let {
                        logApp(res.msg)
                        _quranUiState.value=false
                    }
                }
                Status.SUCCESS -> {
                    res.data?.let {
                        it.data!!.let { surahs->
                            _quranResponse.value=surahs
                            _quranUiState.value=false
                        }
                    }
                }
            }
        }
    }
}