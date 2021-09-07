package aslan.aslanov.prayerapp.model.prayerCurrent


import com.squareup.moshi.Json

data class Hijri(
    @Json(name = "date")
    val date: String?,
    @Json(name = "day")
    val day: String?,
    @Json(name = "designation")
    val designation: DesignationX?,
    @Json(name = "format")
    val format: String?,
    @Json(name = "holidays")
    val holidays: List<Any>?,
    @Json(name = "month")
    val month: MonthX?,
    @Json(name = "weekday")
    val weekday: WeekdayX?,
    @Json(name = "year")
    val year: String?
)