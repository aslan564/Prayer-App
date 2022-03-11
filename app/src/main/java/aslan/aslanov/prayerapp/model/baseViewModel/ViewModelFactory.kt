package aslan.aslanov.prayerapp.model.baseViewModel

import aslan.aslanov.prayerapp.ui.activity.main.MainViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.ui.activity.reading.viewModel.ReadingActivityViewModel
import aslan.aslanov.prayerapp.ui.fragment.ayahs.AyahsViewModel
import aslan.aslanov.prayerapp.ui.fragment.calendar.CalendarViewModel
import aslan.aslanov.prayerapp.ui.fragment.country.CountryViewModel
import aslan.aslanov.prayerapp.ui.fragment.surah.QuranSurahsViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val database: PrayerDatabase,
    private val context: Context,
    private val retrofit: RetrofitService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(database,context,retrofit) as T
            }
            modelClass.isAssignableFrom(CountryViewModel::class.java) -> {
                CountryViewModel(database,context,retrofit) as T
            }
            modelClass.isAssignableFrom(CalendarViewModel::class.java) -> {
                CalendarViewModel() as T
            }
            modelClass.isAssignableFrom(ReadingActivityViewModel::class.java) -> {
                ReadingActivityViewModel(database, context, retrofit) as T
            }
            modelClass.isAssignableFrom(QuranSurahsViewModel::class.java) -> {
                QuranSurahsViewModel(database, context, retrofit) as T
            }
            modelClass.isAssignableFrom(AyahsViewModel::class.java) -> {
                AyahsViewModel(database, context,retrofit) as T
            }
            else -> {
                throw IllegalArgumentException(context.getString(R.string.illegalArgument))
            }
        }
    }
}