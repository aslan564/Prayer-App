package aslan.aslanov.prayerapp.model.countryModel


import com.squareup.moshi.Json

data class CountryResponse(
    @Json(name = "data")
    val `data`: List<CountryData>?,
    @Json(name = "error")
    val error: Boolean?,
    @Json(name = "msg")
    val msg: String?
)