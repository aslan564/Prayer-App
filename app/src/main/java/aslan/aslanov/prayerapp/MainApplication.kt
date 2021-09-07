package aslan.aslanov.prayerapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.worker.PrayerAppWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        SharedPreferenceManager.instance(applicationContext)
        delayInit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun delayInit() = applicationScope.launch {
        setupRecurringWork()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecurringWork() {
        val constraint = Constraints.Builder().setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val dailyWorkRequest =
            PeriodicWorkRequestBuilder<PrayerAppWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraint)
                .build()

        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, dailyWorkRequest)


    }

    companion object {
        private const val TAG = "MainApplication"
        private const val WORK_NAME: String = "aslan.aslanov.prayerapp"
        const val WORK_INTENT_HOURS = "workerInputDataHours"
        const val WORK_INTENT_MINUTE = "workerInputDataMinute"

    }
}