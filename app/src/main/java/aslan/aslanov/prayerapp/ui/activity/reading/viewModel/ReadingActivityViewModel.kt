package aslan.aslanov.prayerapp.ui.activity.reading.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.baseViewModel.BaseViewModel
import aslan.aslanov.prayerapp.model.newQuranModel.Ayah
import aslan.aslanov.prayerapp.model.newQuranModel.QuranResponse
import aslan.aslanov.prayerapp.model.newQuranModel.Surah
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.QuranSurahRepository
import kotlinx.coroutines.launch

class ReadingActivityViewModel(
    database: PrayerDatabase,
    context: Context,
    retrofit: RetrofitService
) : BaseViewModel<QuranResponse>() {
    private val repository = QuranSurahRepository(database, context, retrofit)
    fun getQuran() = viewModelScope.launch {
        repository.fetchNewQuran { result ->
            when (result.status) {
                Status.ERROR -> {
                    result.msg?.let {
                        setError(it)
                        setLoading(false)
                    }
                }
                Status.SUCCESS -> {
                    result.data?.let {
                        setData(result.data) {
                            setLoading(false)
                        }
                    }
                }
                Status.LOADING -> {
                    setLoading(true)
                }
            }
        }
    }

    fun addSurahDb(surahs: List<Surah>) = viewModelScope.launch {
        repository.addQuranSurahToDB(surahs)
    }
}