package aslan.aslanov.prayerapp.repository

import androidx.lifecycle.LiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.countryModel.City

class CitiesRepository(private val database: PrayerDatabase)  {
    fun getAllCity(countryName:String):LiveData<List<City>>{
      return  database.getCountryDao().getCities(countryName)
    }
}