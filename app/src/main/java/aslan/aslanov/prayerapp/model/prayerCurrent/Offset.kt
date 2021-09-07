package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Offset(
    @Json(name = "Asr")
    val asr: Int?,
    @Json(name = "Dhuhr")
    val dhuhr: Int?,
    @Json(name = "Fajr")
    val fajr: Int?,
    @Json(name = "Imsak")
    val imsak: Int?,
    @Json(name = "Isha")
    val isha: Int?,
    @Json(name = "Maghrib")
    val maghrib: Int?,
    @Json(name = "Midnight")
    val midnight: Int?,
    @Json(name = "Sunrise")
    val sunrise: Int?,
    @Json(name = "Sunset")
    val sunset: Int?
)