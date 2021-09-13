package aslan.aslanov.prayerapp.ui.fragment.categoryOfHadeeth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.repository.HadithAndLanguageRepository
import kotlinx.coroutines.launch

class HadithCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database=PrayerDatabase.getInstance(application)
    private val repository = HadithAndLanguageRepository(database)

    val categoryItem = repository.getCategoryOfHadeeths()
    val error = repository.error
    val loading = repository.loading

    fun getHadithCategory() =
        viewModelScope.launch {
            repository.addCategoryToDatabase()
        }
}