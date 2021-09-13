package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.repository.QuranSurahAyahsRepository
import kotlinx.coroutines.launch

class AyahsViewModel(application: Application) : AndroidViewModel(application) {
    private val database=PrayerDatabase.getInstance(application)
    private val repository = QuranSurahAyahsRepository(database)


    val errorMsg = repository.baseErrorMessage
    val ayahsStatus = repository.baseLoading

    fun fetchSurahAyahs(
        surah: Int,
        edition: String
    ) = viewModelScope.launch {
        repository.fetchSurahAyahs(surah, edition)
    }

    fun getAyahsFromDatabase(surah: Int, onCompleteListener: (LiveData<List<AyahEntity>>) -> Unit) {
        repository.getAyahsFromDatabase(surah){
            onCompleteListener(it)
        }
    }

}