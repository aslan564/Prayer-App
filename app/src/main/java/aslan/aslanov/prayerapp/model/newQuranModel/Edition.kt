package aslan.aslanov.prayerapp.model.newQuranModel


import com.squareup.moshi.Json

data class Edition(
    @Json(name = "englishName")
    val englishName: String?,
    @Json(name = "format")
    val format: String?,
    @Json(name = "identifier")
    val identifier: String?,
    @Json(name = "language")
    val language: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "type")
    val type: String?
)