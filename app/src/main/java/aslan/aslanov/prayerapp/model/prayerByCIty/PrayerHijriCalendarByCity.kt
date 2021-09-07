package aslan.aslanov.prayerapp.model.prayerByCIty


import aslan.aslanov.prayerapp.model.prayerCurrent.Data
import com.squareup.moshi.Json

data class PrayerHijriCalendarByCity(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "data")
    val `data`: List<Data>?,
    @Json(name = "status")
    val status: String?
)