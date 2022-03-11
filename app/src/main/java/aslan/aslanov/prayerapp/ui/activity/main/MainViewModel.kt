package aslan.aslanov.prayerapp.ui.activity.main

import android.content.Context
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.model.baseViewModel.BaseViewModel
import aslan.aslanov.prayerapp.model.countryModel.CountryResponse
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.CountryListRepository
import aslan.aslanov.prayerapp.util.logApp
import kotlinx.coroutines.launch

class MainViewModel(
    database: PrayerDatabase,
    context: Context,
    retrofit: RetrofitService
) : BaseViewModel<CountryResponse>() {
    private val repository by lazy { CountryListRepository(database,context,retrofit) }

    fun getAllCountry() = viewModelScope.launch {
        if (SharedPreferenceManager.isFirstTime) {
            SharedPreferenceManager.isFirstTime = false
            repository.getAllCountry { result ->
                when (result.status) {
                    Status.LOADING -> {
                        setLoading(true)
                    }
                    Status.SUCCESS -> {
                        result.data?.let {
                            setData(it) {
                                setLoading(false)
                            }
                        }
                    }
                    Status.ERROR -> {
                        result.msg?.let {
                            setError(it)
                            setLoading(false)
                        }
                    }

                }
            }
        } else {
            logApp("ilk girisiniz deyil")
            setError("ilk girisiniz deyil")
        }
    }

    fun saveConvertedList(countryResponse: CountryResponse)=viewModelScope.launch {
        repository.convertedList(countryResponse)
    }
}