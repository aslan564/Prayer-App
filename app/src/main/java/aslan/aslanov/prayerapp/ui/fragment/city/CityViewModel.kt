package aslan.aslanov.prayerapp.ui.fragment.city

import android.app.Application
import androidx.lifecycle.*
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.countryModel.City
import aslan.aslanov.prayerapp.repository.CitiesRepository
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PrayerDatabase.getInstance(application)
    private val repository = CitiesRepository(database)



    fun getAllCity(countryNAme: String,onComplete:(LiveData<List<City>>)->Unit) = viewModelScope.launch {
        val cities = repository.getAllCity(countryNAme)
        onComplete(cities)
    }
}