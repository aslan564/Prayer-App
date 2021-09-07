package aslan.aslanov.prayerapp.model.surahs


import com.squareup.moshi.Json

data class Data(
    @Json(name = "englishName")
    val englishName: String?,
    @Json(name = "englishNameTranslation")
    val englishNameTranslation: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "number")
    val number: Int?,
    @Json(name = "numberOfAyahs")
    val numberOfAyahs: Int?,
    @Json(name = "revelationType")
    val revelationType: String?
)