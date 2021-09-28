package aslan.aslanov.prayerapp.ui.fragment.country

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.model.countryModel.Data
import aslan.aslanov.prayerapp.repository.CountryListRepository
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PrayerDatabase.getInstance(application)
    private val repository = CountryListRepository(database)

    val country=repository.getCountryDatabase()

    val state=repository.uiState

    val countryListError = repository.countryListError


     fun getAllCountry() = viewModelScope.launch {
        if (!SharedPreferenceManager.isFirstTime) {
            SharedPreferenceManager.isFirstTime = true
            repository.convertedList { list: List<Data>?, b: Boolean ->

                Log.d(TAG, "getAllCountry: $list")
            }
        } else {
            Log.d(TAG, "getAllCountry: $1 giris deyil")
        }

    }

    companion object {
        private const val TAG = "MainActivityViewModel"
    }
}