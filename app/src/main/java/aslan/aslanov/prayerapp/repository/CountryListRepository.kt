package aslan.aslanov.prayerapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.countryModel.City
import aslan.aslanov.prayerapp.model.countryModel.Country
import aslan.aslanov.prayerapp.model.countryModel.CountryData
import aslan.aslanov.prayerapp.model.countryModel.CountryResponse
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.util.catchServerError
import aslan.aslanov.prayerapp.util.cityList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CountryListRepository(
    private val database: PrayerDatabase,
    private val context: Context,
    private val retrofit: RetrofitService
) {

    suspend fun getAllCountry(onCompletionListener: (NetworkResult<CountryResponse>) -> Unit) {
        try {
            onCompletionListener(NetworkResult.loading())
            val result = retrofit.getCountryList.getAllCountry()
            if (result.isSuccessful) {
                result.body()?.let {
                    onCompletionListener(NetworkResult.success(it))
                } ?: onCompletionListener(NetworkResult.error(result.message()))
            } else {
                catchServerError<CountryResponse>(result.errorBody()) {
                    onCompletionListener(it)
                }
            }
        } catch (e: Exception) {
            onCompletionListener(NetworkResult.error(e.message))
        }
    }

    suspend fun getCountryDatabase() = database.getCountryDao().getCountryWithCities()

    suspend fun convertedList(countryResponse: CountryResponse) {
        try {
            countryResponse.data?.let {
                for (item in countryResponse.data) {
                    addCountryToDatabase(Country(item.country!!))
                    addCityToDatabase(
                        cityList(
                            item.cities!!,
                            countryId = item.country
                        )
                    )
                }
            }
        } catch (e: Exception) {

        }
    }

    private suspend fun addCityToDatabase(countryWithCities: List<City>) {
        database.getCountryDao().insertCity(*countryWithCities.toTypedArray())
    }

    private suspend fun addCountryToDatabase(country: Country) {
        database.getCountryDao().insertCountry(country)
    }
}