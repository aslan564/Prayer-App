package aslan.aslanov.prayerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.model.prayerCurrent.CurrentDayPrayerResponse
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.util.catchServerError

class PrayerTimingsRepository(private val database: PrayerDatabase) {
    private var _baseErrorMessage = MutableLiveData<String>()
    val baseErrorMessage: LiveData<String>
        get() = _baseErrorMessage


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
                } ?: onLoadCompleteListener(NetworkResult.error(res.message()))
            } else {
                catchServerError<CurrentDayPrayerResponse>(res.errorBody()) {
                    onLoadCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onLoadCompleteListener(NetworkResult.error(e.message))
        }
    }

    suspend fun addTimeToDatabase(time: TimingsEntity) {
        database.getCountryDao().insertTime(time)
    }

    fun getCurrentTimeLive(): LiveData<TimingsEntity> {
        return database.getCountryDao().getTimingsEntityLive()
    }

    fun getRandomAyahFromQuran(): LiveData<List<AyahEntity>> {
        return database.getQuranDao().getRandomAyahsFromDatabase()
    }

    suspend fun getRandomAyahFromQuranList(): List<AyahEntity> {
        return database.getQuranDao().getRandomAyahsListFromDatabase()
    }

    fun getRandomHadeethsFromQuran(): LiveData<List<HadeethsEntity>> {
        return database.getHadeeth().getRandomHadeethsFromQuran()
    }
}