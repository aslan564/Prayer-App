package aslan.aslanov.prayerapp.repository

import aslan.aslanov.prayerapp.model.prayerByCIty.PrayerHijriCalendarByCity
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService.getPrayerTimeForMonth
import aslan.aslanov.prayerapp.util.getServerError
import java.lang.Exception

class MonthTimingPrayerRepository {
    suspend fun getPrayerTimeForMonth(onCompletionListener: (NetworkResult<PrayerHijriCalendarByCity>) -> Unit) {
        try {
            onCompletionListener(NetworkResult.loading())
            val res = getPrayerTimeForMonth.getPrayerTimeByCity("Baki", "Azerbaijan", 2, 4, 1437)
            if (res.isSuccessful && res.code() == 200) {
                res.body()?.let {
                    onCompletionListener(NetworkResult.success(it))
                } ?: onCompletionListener(NetworkResult.error(res.message()))
            } else {
                getServerError<PrayerHijriCalendarByCity>(res.errorBody()) {
                    onCompletionListener(it)
                }
            }
        } catch (e: Exception) {
            onCompletionListener(NetworkResult.error(e.message))
        }
    }
}