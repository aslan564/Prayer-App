package aslan.aslanov.prayerapp.ui.fragment.hadeeths

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.repository.HadithAndLanguageRepository
import aslan.aslanov.prayerapp.repository.WhereWereRepository
import kotlinx.coroutines.launch

class HadeethsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PrayerDatabase.getInstance(application)
    private val repository by lazy { HadithAndLanguageRepository(database) }
    private val repositoryWhereWereWe by lazy { WhereWereRepository(database) }
    private var _whereWereWe = MutableLiveData<WhereWereWe>()
    val whereWereLiveData: LiveData<WhereWereWe>
        get() = _whereWereWe


    val error = repository.error
    val loading = repository.loading

    fun addHadith(
        categoryId: Int,
        page: Int,
        perPage: Int,
        hadeethsName: String,
    ) = viewModelScope.launch {
        repository.addHadithToDatabase(categoryId, page, perPage, hadeethsName)
    }

    fun getHadithFromDb(
        categoryId: Int
    ): LiveData<List<HadeethsEntity>> {
        return repository.getHadithFromDb(categoryId)
    }

    fun setWhereWee(whereWereWe: WhereWereWe) = viewModelScope.launch {
        repositoryWhereWereWe.setPositionFromDatabase(whereWereWe)
    }

    fun getWhereWee(id: Int) = viewModelScope.launch {
        _whereWereWe.value = repositoryWhereWereWe.getPositionFromDatabase(id)
    }

}