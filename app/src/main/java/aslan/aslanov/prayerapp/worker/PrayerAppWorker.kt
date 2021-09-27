package aslan.aslanov.prayerapp.worker

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.mainService.AlarmReceiver
import aslan.aslanov.prayerapp.mainService.EXTRA_MESSAGE
import aslan.aslanov.prayerapp.mainService.EXTRA_TYPE
import aslan.aslanov.prayerapp.model.whereWereWe.AyahsOrSurah
import aslan.aslanov.prayerapp.repository.PrayerTimingsRepository
import aslan.aslanov.prayerapp.util.PendingRequests.REQUEST_CODE_AYAHS
import aslan.aslanov.prayerapp.util.PendingRequests.REQUEST_CODE_SALAWAT
import aslan.aslanov.prayerapp.util.logApp
import java.util.*

class PrayerAppWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {
    private val currentDay = Calendar.getInstance(Locale.getDefault())

    override suspend fun doWork(): Result {
        return try {
            val database = PrayerDatabase.getInstance(applicationContext)
            val repository by lazy { PrayerTimingsRepository(database) }
            if (currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                makeSalawat()
            } else {
                logApp("${currentDay.get(Calendar.DAY_OF_WEEK)}")
            }

            makeDailyAyahRemainder(repository)

            if (SharedPreferenceManager.locationCityName != null && SharedPreferenceManager.locationCountryName != null) {
                repository.getPrayerTimings(
                    SharedPreferenceManager.locationCityName!!,
                    SharedPreferenceManager.locationCountryName!!,
                    8
                ) {
                    Log.d(TAG, "doWork: $it")
                }
            } else {
                repository.getPrayerTimings(
                    "Baku",
                    "Azerbaijan",
                    1
                ) {
                    Log.d(TAG, "doWork: $it")
                }
            }

            Result.success()
        } catch (ex: Exception) {
            Result.retry()
        }
    }

    private suspend fun makeDailyAyahRemainder(repository: PrayerTimingsRepository) {
        val ayahs = repository.getRandomAyahFromQuranList()
        val random = Random()
        val ayah = ayahs[random.nextInt(ayahs.size)]
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, AyahsOrSurah.AYAHS.name)
        intent.putExtra(EXTRA_MESSAGE, ayah.text)
        AlarmReceiver.showAlarmNotification(applicationContext, intent, REQUEST_CODE_AYAHS)
    }

    private fun makeSalawat() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(p0: Long) {
                Log.d(TAG, "onTick: $p0")
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, AlarmReceiver::class.java)
                intent.putExtra(EXTRA_TYPE, AyahsOrSurah.SALAWAT.name)
                intent.putExtra(EXTRA_MESSAGE, "makeSalawat")
                AlarmReceiver.showAlarmNotification(
                    applicationContext,
                    intent,
                    REQUEST_CODE_SALAWAT
                )
            }
        }.start()
    }


    companion object {
        private const val TAG = "PrayerAppWorker"
    }
}