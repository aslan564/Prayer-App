package aslan.aslanov.prayerapp.ui.fragment.timings

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.PrayerTimingsRepository
import aslan.aslanov.prayerapp.util.listTimingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrentTimingsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PrayerDatabase.getInstance(application)
    private val repository by lazy { PrayerTimingsRepository(database) }


    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val prayerTimingsLive = repository.getCurrentTimeLive()


    fun getTimingsPrayer(times: String, country: String, method: Int) =
        viewModelScope.launch {
            repository.getPrayerTimings(times, country, method) { res ->
                when (res.status) {
                    Status.ERROR -> {
                        res.msg?.let {
                            _errorMessage.value = it
                            _loading.value = false
                        }
                    }
                    Status.LOADING -> {
                        _loading.value = true
                    }
                    Status.SUCCESS -> {
                        res.data?.let {
                            _loading.value = false
                            GlobalScope.launch(Dispatchers.IO) {
                                repository.addTimeToDatabase(listTimingsEntity(it.data!!.timings))
                            }
                        }
                    }
                }
            }
        }

}