package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Date(
    @Json(name = "gregorian")
    val gregorian: Gregorian?,
    @Json(name = "hijri")
    val hijri: Hijri?,
    @Json(name = "readable")
    val readable: String?,
    @Json(name = "timestamp")
    val timestamp: String?
)