package aslan.aslanov.prayerapp.ui.fragment.ayahs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.model.ayahs.Ayah
import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.QuranSurahAyahsRepository
import aslan.aslanov.prayerapp.util.logApp
import kotlinx.coroutines.launch

class AyahsViewModel : ViewModel() {
    private val repository = QuranSurahAyahsRepository()

    private var _ayahs = MutableLiveData<AyahsResponse>()
    val ayahs: LiveData<AyahsResponse>
        get() = _ayahs
    private var _ayahsStatus = MutableLiveData<Boolean>()
    val ayahsStatus: LiveData<Boolean>
        get() = _ayahsStatus


    fun fetchSurahAyahs(
        surah: Int,
        edition: String
    ) = viewModelScope.launch {
        repository.fetchSurahAyahsFromQuran(surah, edition) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    result.data?.let {
                        _ayahs.value=it
                        _ayahsStatus.value=false
                        logApp(it.data!!.ayahs!!.toString())
                    }
                }
                Status.LOADING -> {
                    _ayahsStatus.value=true
                }
                Status.ERROR -> {
                    result.msg?.let {
                        _ayahsStatus.value=false
                    }
                }
            }
        }
    }
}