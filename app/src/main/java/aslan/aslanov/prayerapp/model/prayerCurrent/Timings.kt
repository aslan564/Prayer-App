package aslan.aslanov.prayerapp.model.prayerCurrent


import androidx.room.Entity
import androidx.room.PrimaryKey
import aslan.aslanov.prayerapp.util.Prayers
import com.squareup.moshi.Json
import java.util.Date

data class Timings(
    @Json(name = "Asr")
    val asr: String?,
    @Json(name = "Dhuhr")
    val dhuhr: String?,
    @Json(name = "Fajr")
    val fajr: String?,
    @Json(name = "Imsak")
    val imsak: String?,
    @Json(name = "Isha")
    val isha: String?,
    @Json(name = "Maghrib")
    val maghrib: String?,
    @Json(name = "Midnight")
    val midnight: String?,
    @Json(name = "Sunrise")
    val sunrise: String?,
    @Json(name = "Sunset")
    val sunset: String?
)
@Entity(tableName = "table_timings")
data class TimingsEntity(
    @PrimaryKey(autoGenerate = false)
    val timeId:Int=1,
    val asr: String?,
    val dhuhr: String?,
    val fajr: String?,
    val imsak: String?,
    val isha: String?,
    val maghrib: String?,
    val midnight: String?,
    val sunrise: String?,
    val sunset: String?
)
data class TimingsConverted(
    val prayerID: Int,
    val prayerName: Prayers,
    val prayerTime: Date,
)