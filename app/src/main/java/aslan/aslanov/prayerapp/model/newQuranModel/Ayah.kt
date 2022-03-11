package aslan.aslanov.prayerapp.model.newQuranModel


import com.squareup.moshi.Json

data class Ayah(
    @Json(name = "hizbQuarter")
    val hizbQuarter: Int?,
    @Json(name = "juz")
    val juz: Int?,
    @Json(name = "manzil")
    val manzil: Int?,
    @Json(name = "number")
    val number: Int?,
    @Json(name = "numberInSurah")
    val numberInSurah: Int?,
    @Json(name = "page")
    val page: Int?,
    @Json(name = "ruku")
    val ruku: Int?,
    @Json(name = "sajda")
    val sajda: Any?,
    @Json(name = "text")
    val text: String?
)