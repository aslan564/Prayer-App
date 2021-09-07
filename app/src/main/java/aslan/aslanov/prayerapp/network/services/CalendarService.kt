package aslan.aslanov.prayerapp.network.services

import aslan.aslanov.prayerapp.model.prayerByCIty.PrayerHijriCalendarByCity
import aslan.aslanov.prayerapp.model.prayerCurrent.CurrentDayPrayerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarService {

    @GET("hijriCalendarByCity")
    suspend fun getPrayerTimeByCity(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<PrayerHijriCalendarByCity>

    @GET("timingsByCity")
    suspend fun getPrayerTimingsByCity(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int,
    ): Response<CurrentDayPrayerResponse>

    @GET("currentTimestamp")
    suspend fun getTimeByCity(
        @Query("zone") city: String,
    ): Response<CurrentDayPrayerResponse>

}