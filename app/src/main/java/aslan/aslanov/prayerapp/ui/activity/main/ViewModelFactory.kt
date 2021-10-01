package aslan.aslanov.prayerapp.ui.activity.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.ui.fragment.calendar.CalendarViewModel
import aslan.aslanov.prayerapp.ui.fragment.country.CountryViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(context = context) as T
            }
            modelClass.isAssignableFrom(CountryViewModel::class.java) -> {
                CountryViewModel(context) as T
            }
            modelClass.isAssignableFrom(CalendarViewModel::class.java) -> {
                CalendarViewModel() as T
            }
            else -> {
                throw IllegalArgumentException(context.getString(R.string.illegalArgument))
            }
        }
    }

}