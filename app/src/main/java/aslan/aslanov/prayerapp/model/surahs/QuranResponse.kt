package aslan.aslanov.prayerapp.model.surahs


import com.squareup.moshi.Json

data class QuranResponse(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "data")
    val `data`: List<Data>?,
    @Json(name = "status")
    val status: String?
)