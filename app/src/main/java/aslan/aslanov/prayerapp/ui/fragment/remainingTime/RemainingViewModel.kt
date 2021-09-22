package aslan.aslanov.prayerapp.ui.fragment.remainingTime

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.repository.PrayerTimingsRepository

class RemainingViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PrayerDatabase.getInstance(application)
    private val repository = PrayerTimingsRepository(database)

    val currentTime = repository.getCurrentTimeLive()

    val randomAyah = repository.getRandomAyahFromQuran()
    val randomHadeeths = repository.getRandomHadeethsFromQuran()

}