package aslan.aslanov.prayerapp.model.ayahs


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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

@Entity(tableName = "table_ayahs")
data class AyahEntity(
    @ColumnInfo(name = "hizbQuarter")
    val hizbQuarter: Int,
    @ColumnInfo(name = "juz")
    val juz: Int,
    @ColumnInfo(name = "manzil")
    val manzil: Int,
    @PrimaryKey(autoGenerate = false)
    val number: Int,
    @ColumnInfo(name = "numberInSurah")
    val numberInSurah: Int,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "ruku")
    val ruku: Int,
    @ColumnInfo(name = "sajda")
    val sajda: Boolean,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "surahId")
    val surahId: Int ,
    @ColumnInfo(name = "surahEnglishName")
    val surahEnglishName: String,
    @ColumnInfo(name = "surahArabicName")
    val surahArabicName: String
)