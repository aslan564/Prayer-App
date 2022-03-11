package aslan.aslanov.prayerapp.model.newQuranModel


import com.squareup.moshi.Json

data class QuranData(
    @Json(name = "edition")
    val edition: Edition?,
    @Json(name = "surahs")
    val surahs: List<Surah>?
)