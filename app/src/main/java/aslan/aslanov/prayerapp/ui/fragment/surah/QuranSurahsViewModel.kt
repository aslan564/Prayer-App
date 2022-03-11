package aslan.aslanov.prayerapp.ui.fragment.surah

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.model.baseViewModel.BaseViewModel
import aslan.aslanov.prayerapp.model.newQuranModel.Ayah
import aslan.aslanov.prayerapp.model.newQuranModel.Surah
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.QuranSurahRepository
import com.google.android.gms.tasks.NativeOnCompleteListener
import kotlinx.coroutines.launch

class QuranSurahsViewModel(
    database: PrayerDatabase,
    context: Context,
    retrofit: RetrofitService
) : BaseViewModel<List<SurahEntity>>() {
    private val repository = QuranSurahRepository(database, context, retrofit)
    private var _ayahResponse = MutableLiveData<AyahsResponse>()
    val ayahResponse: LiveData<AyahsResponse>
        get() = _ayahResponse


    fun fetchSurahs() = viewModelScope.launch {
        setData(repository.getSurahFromDB()) {
            setLoading(false)
        }
    }

    fun getAyahFromInternet(
        surah: Int,
        language: String,
        onCompleteListener: (List<Ayah>)->Unit
    ) = viewModelScope.launch {
        repository.getAyahFromInternet(surah, language) { networkResult ->
            when (networkResult.status) {
                Status.ERROR -> {
                    networkResult.msg?.let {
                        setError(it)
                        setLoading(false)
                    }
                }
                Status.LOADING -> {
                    setLoading(true)
                }
                Status.SUCCESS -> {
                    networkResult.data?.let { ayahsResponse ->
                        ayahsResponse.data?.ayahs?.let { onCompleteListener(it) }
                        setLoading(false)
                    }
                }
            }

        }
    }

    fun addAyahDb(surahs: SurahEntity, ayahs: List<Ayah>) = viewModelScope.launch {
        repository.addQuranAyahsToDB(surahs, ayahs)
    }

}