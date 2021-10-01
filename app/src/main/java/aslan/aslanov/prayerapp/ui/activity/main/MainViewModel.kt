package aslan.aslanov.prayerapp.ui.activity.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.model.countryModel.Data
import aslan.aslanov.prayerapp.repository.CountryListRepository
import aslan.aslanov.prayerapp.util.logApp
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {
    private val database by lazy { PrayerDatabase.getInstance(context) }
    private val repository by lazy { CountryListRepository(database) }
    fun getAllCountry() = viewModelScope.launch {
        if (!SharedPreferenceManager.isFirstTime) {
            SharedPreferenceManager.isFirstTime = true
            repository.convertedList { list: List<Data>?, b: Boolean ->
                logApp("getAllCountry: $list")
            }
        } else {
            logApp("ilk girisiniz deyil")
        }
    }
}