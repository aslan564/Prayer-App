package aslan.aslanov.prayerapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.countryModel.Country
import aslan.aslanov.prayerapp.model.countryModel.CountryResponse
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.model.countryModel.Data
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService.getCountryList
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.util.cityList
import aslan.aslanov.prayerapp.util.getServerError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CountryListRepository(private val database: PrayerDatabase) {

    private var _countryListError = MutableLiveData<String>()
    val countryListError: LiveData<String>
        get() = _countryListError


    private suspend fun getAllCountry(onCompletionListener: (NetworkResult<CountryResponse>) -> Unit) {
        try {
            onCompletionListener(NetworkResult.loading())
            val result = getCountryList.getAllCountry()
            if (result.isSuccessful && result.code() == 200) {
                result.body()?.let {
                    onCompletionListener(NetworkResult.success(it))
                } ?: onCompletionListener(NetworkResult.error(result.message()))
            } else {
                getServerError<CountryResponse>(result.errorBody()) {
                    onCompletionListener(it)
                }
            }
        } catch (e: Exception) {
            onCompletionListener(NetworkResult.error(e.message))
        }
    }

    fun getCountryDatabase() :LiveData<List<CountryWithCities>>{
        return database.getDao().getCountryWithCities()
    }

    suspend fun convertedList(onCompletionListener: (List<Data>?, Boolean) -> Unit) {
        try {
            getAllCountry { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        res.data?.let {
                            onCompletionListener(res.data.data, false)
                            Log.d("RemainingTimeFragment", "addCountryDatabase: $it")
                            GlobalScope.launch {
                                for (item in it.data!!) {
                                    addCityAndCountryToDatabase(
                                        CountryWithCities(
                                            Country(item.country!!),
                                            cityList(item.cities!!, countryId = item.country)
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        res.msg?.let {
                            _countryListError.value = it
                            onCompletionListener(null, false)
                        }
                    }
                    Status.LOADING -> {
                        onCompletionListener(null, true)
                    }
                }

            }
        } catch (e: Exception) {

        }
    }

    private suspend fun addCityAndCountryToDatabase(countryWithCities: CountryWithCities) {
        database.getDao().insertCity(*countryWithCities.city.toTypedArray())
        database.getDao().insertCountry(countryWithCities.country)
    }
}