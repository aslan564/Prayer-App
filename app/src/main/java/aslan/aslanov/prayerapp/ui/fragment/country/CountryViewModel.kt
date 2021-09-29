package aslan.aslanov.prayerapp.ui.fragment.country

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.model.countryModel.Data
import aslan.aslanov.prayerapp.repository.CountryListRepository
import kotlinx.coroutines.launch

class CountryViewModel(context: Context) : ViewModel() {
    private val database = PrayerDatabase.getInstance(context)
    private val repository = CountryListRepository(database)

    val country = repository.getCountryDatabase()

    val state = repository.uiState

    val countryListError = repository.countryListError

    companion object {
        private const val TAG = "MainActivityViewModel"
    }
}