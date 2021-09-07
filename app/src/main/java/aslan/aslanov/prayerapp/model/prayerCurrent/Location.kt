package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Location(
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "longitude")
    val longitude: Double?
)