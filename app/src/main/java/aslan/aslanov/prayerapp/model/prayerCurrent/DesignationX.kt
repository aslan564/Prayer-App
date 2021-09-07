package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class DesignationX(
    @Json(name = "abbreviated")
    val abbreviated: String?,
    @Json(name = "expanded")
    val expanded: String?
)