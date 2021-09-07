package aslan.aslanov.prayerapp.ui.fragment.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.prayerByCIty.PrayerHijriCalendarByCity
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.MonthTimingPrayerRepository
import aslan.aslanov.prayerapp.repository.PrayerTimingsRepository
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {
    private val repository by lazy { MonthTimingPrayerRepository() }

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _prayerTimeByHijriCalendar = MutableLiveData<PrayerHijriCalendarByCity>()
    val prayerTimeByHijriCalendar: LiveData<PrayerHijriCalendarByCity>
        get() = _prayerTimeByHijriCalendar


    fun getMonthTimingByCity() = viewModelScope.launch {
        repository.getPrayerTimeForMonth { response ->
            when (response.status) {
                Status.LOADING -> {
                    _loading.value=true
                }
                Status.SUCCESS -> {
                    response.data?.let {
                        _prayerTimeByHijriCalendar.value=it
                        _loading.value=false
                    }
                }
                Status.ERROR -> {
                    response.msg?.let {
                        _errorMessage.value=it
                        _loading.value=false
                    }
                }
            }
        }
    }
}