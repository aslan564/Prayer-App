package aslan.aslanov.prayerapp.model.language


import com.squareup.moshi.Json

data class LanguageResponse(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "data")
    val `data`: List<Data>?,
    @Json(name = "status")
    val status: String?
)