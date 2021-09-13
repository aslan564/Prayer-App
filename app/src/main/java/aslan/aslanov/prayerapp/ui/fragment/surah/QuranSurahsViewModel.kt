package aslan.aslanov.prayerapp.ui.fragment.surah

import android.app.Application
import androidx.lifecycle.*
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.surahs.Data
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.QuranSurahRepository
import aslan.aslanov.prayerapp.util.logApp
import kotlinx.coroutines.launch

class QuranSurahsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PrayerDatabase.getInstance(application)
    private val repository = QuranSurahRepository(database)


    val quranUiState = repository.quranUiState

    val errorMessage = repository.errorMessage

    fun getSurahFromDB(onCompleteListener: (LiveData<List<SurahEntity>>) -> Unit) =
        viewModelScope.launch {
            repository.getSurahFromDB{
                onCompleteListener(it)
            }
        }

}