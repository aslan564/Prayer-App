package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Weekday(
    @Json(name = "en")
    val en: String?
)