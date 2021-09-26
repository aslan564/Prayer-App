package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.repository.QuranSurahAyahsRepository
import aslan.aslanov.prayerapp.repository.WhereWereRepository
import kotlinx.coroutines.launch

class AyahsViewModel(application: Application) : AndroidViewModel(application) {
    private val database=PrayerDatabase.getInstance(application)
    private val repository = QuranSurahAyahsRepository(database)
    private val repositoryWhereWereWe by lazy { WhereWereRepository(database) }
    private var _whereWereWe = MutableLiveData<WhereWereWe>()
    val whereWereLiveData: LiveData<WhereWereWe>
        get() = _whereWereWe


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

    fun setWhereWee(whereWereWe: WhereWereWe) = viewModelScope.launch {
        repositoryWhereWereWe.setPositionFromDatabase(whereWereWe)
    }

    fun getWhereWee(id: Int) = viewModelScope.launch {
        _whereWereWe.value = repositoryWhereWereWe.getPositionFromDatabase(id)
    }


}