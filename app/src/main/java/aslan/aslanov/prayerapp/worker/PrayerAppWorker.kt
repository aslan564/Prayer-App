package aslan.aslanov.prayerapp.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.mainService.AlarmReceiver
import aslan.aslanov.prayerapp.mainService.EXTRA_MESSAGE
import aslan.aslanov.prayerapp.mainService.EXTRA_TYPE
import aslan.aslanov.prayerapp.repository.PrayerTimingsRepository
import java.util.*

class PrayerAppWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {

    override suspend fun doWork(): Result {
        return try {
            val database = PrayerDatabase.getInstance(applicationContext)
            val repository by lazy { PrayerTimingsRepository(database) }

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
        intent.putExtra(EXTRA_TYPE, ayah.surahEnglishName)
        intent.putExtra(EXTRA_MESSAGE, ayah.text)
        AlarmReceiver.showAlarmNotification(applicationContext, intent)
    }


    companion object {
        private const val TAG = "PrayerAppWorker"
        const val WORKER_TAG = "PrayerAppWorkerDateTime"
    }
}