package aslan.aslanov.prayerapp.model.newQuranModel


import com.squareup.moshi.Json

data class Surah(
    @Json(name = "number")
    val number: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "englishName")
    val englishName: String?,
    @Json(name = "englishNameTranslation")
    val englishNameTranslation: String?,
    @Json(name = "revelationType")
    val revelationType: String?
)