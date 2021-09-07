package aslan.aslanov.prayerapp.repository

import androidx.lifecycle.LiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.prayerCurrent.CurrentDayPrayerResponse
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.util.getServerError

class PrayerTimingsRepository(private val database: PrayerDatabase) {

    suspend fun getPrayerTimings(
        city: String, country: String, method: Int,
        onLoadCompleteListener: (NetworkResult<CurrentDayPrayerResponse>) -> Unit
    ) {
        try {
            onLoadCompleteListener(NetworkResult.loading())
            val res = RetrofitService.getPrayerTimeForMonth.getPrayerTimingsByCity(
                city, country, method
            )
            if (res.isSuccessful) {
                res.body()?.let {
                    onLoadCompleteListener(NetworkResult.success(it))
                } ?: onLoadCompleteListener(NetworkResult.error("response body must null"))
            } else {
                getServerError<CurrentDayPrayerResponse>(res.errorBody()) {
                    onLoadCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onLoadCompleteListener(NetworkResult.error(e.message))
        }
    }

    suspend fun addTimeToDatabase(time: TimingsEntity) {
        database.getDao().insertTime(time)
    }

    fun getCurrentTimeLive(): LiveData<TimingsEntity> {
        return database.getDao().getTimingsEntityLive()
    }

}