package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Meta(
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "latitudeAdjustmentMethod")
    val latitudeAdjustmentMethod: String?,
    @Json(name = "longitude")
    val longitude: Double?,
    @Json(name = "method")
    val method: Method?,
    @Json(name = "midnightMode")
    val midnightMode: String?,
    @Json(name = "offset")
    val offset: Offset?,
    @Json(name = "school")
    val school: String?,
    @Json(name = "timezone")
    val timezone: String?
)