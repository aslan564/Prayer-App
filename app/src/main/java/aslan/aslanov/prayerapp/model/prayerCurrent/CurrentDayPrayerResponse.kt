package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class CurrentDayPrayerResponse(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "status")
    val status: String?
)