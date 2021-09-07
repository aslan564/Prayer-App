package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Gregorian(
    @Json(name = "date")
    val date: String?,
    @Json(name = "day")
    val day: String?,
    @Json(name = "designation")
    val designation: Designation?,
    @Json(name = "format")
    val format: String?,
    @Json(name = "month")
    val month: Month?,
    @Json(name = "weekday")
    val weekday: Weekday?,
    @Json(name = "year")
    val year: String?
)