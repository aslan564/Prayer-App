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

    private var _country = MutableLiveData<List<CountryWithCities>>()
    val country: LiveData<List<CountryWithCities>>
        get() = _country

    fun getCountryDatabase() = viewModelScope.launch {
        _country.value = repository.getCountryDatabase()
    }


    private var _stateProgress = MutableLiveData<Boolean>()
    val state: LiveData<Boolean>
        get() = _stateProgress

    val countryListError = repository.countryListError

    init {
        getAllCountry()
    }

    private fun getAllCountry() = viewModelScope.launch {
        if (!SharedPreferenceManager.isFirstTime) {
            SharedPreferenceManager.isFirstTime = true
            repository.convertedList { list: List<Data>?, b: Boolean ->
                _stateProgress.value = b
                Log.d(TAG, "getAllCountry: $list")
            }
        } else {
            _stateProgress.value = false
            Log.d(TAG, "getAllCountry: $1 giris deyil")
        }

    }

    companion object {
        private const val TAG = "MainActivityViewModel"
    }
}