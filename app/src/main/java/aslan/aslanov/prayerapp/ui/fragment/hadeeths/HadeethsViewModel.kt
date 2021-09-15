package aslan.aslanov.prayerapp.ui.fragment.hadeeths

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.repository.HadithAndLanguageRepository
import kotlinx.coroutines.launch

class HadeethsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PrayerDatabase.getInstance(application)
    private val repository = HadithAndLanguageRepository(database)

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


}