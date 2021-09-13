package aslan.aslanov.prayerapp.model.language.category


import com.squareup.moshi.Json

data class HadithLanguageItem(
    @Json(name = "code")
    val code: String?,
    @Json(name = "native")
    val native: String?
)