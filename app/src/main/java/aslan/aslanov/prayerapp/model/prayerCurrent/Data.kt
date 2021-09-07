package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Data(
    @Json(name = "date")
    val date: Date?,
    @Json(name = "meta")
    val meta: Meta?,
    @Json(name = "timings")
    val timings: Timings?
)