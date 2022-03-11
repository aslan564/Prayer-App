package aslan.aslanov.prayerapp.model.newQuranModel


import com.squareup.moshi.Json

data class QuranResponse(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "data")
    val `data`: List<Surah>?,
    @Json(name = "status")
    val status: String?
)