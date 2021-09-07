package aslan.aslanov.prayerapp.model.ayahs


import com.squareup.moshi.Json

data class AyahsResponse(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "status")
    val status: String?
)