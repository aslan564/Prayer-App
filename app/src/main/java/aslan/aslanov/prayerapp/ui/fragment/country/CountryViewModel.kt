package aslan.aslanov.prayerapp.ui.fragment.country

import android.content.Context
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.baseViewModel.BaseViewModel
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.repository.CountryListRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class CountryViewModel(database: PrayerDatabase, context: Context,retrofit: RetrofitService) : BaseViewModel<List<CountryWithCities>>() {
    private val repository = CountryListRepository(database,context, retrofit)


    fun getCountry() = viewModelScope.launch {
        setLoading(true)
        setData(repository.getCountryDatabase()) {
            setLoading(false)
        }
    }

}