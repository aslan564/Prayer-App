package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Params(
    @Json(name = "Fajr")
    val fajr: Double?,
    @Json(name = "Isha")
    val isha: String?
)