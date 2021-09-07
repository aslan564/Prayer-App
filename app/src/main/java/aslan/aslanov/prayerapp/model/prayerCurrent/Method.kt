package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Method(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "location")
    val location: Location?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "params")
    val params: Params?
)