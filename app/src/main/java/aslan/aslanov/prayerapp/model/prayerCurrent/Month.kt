package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Month(
    @Json(name = "en")
    val en: String?,
    @Json(name = "number")
    val number: Int?
)