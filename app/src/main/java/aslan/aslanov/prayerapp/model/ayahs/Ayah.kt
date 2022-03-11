package aslan.aslanov.prayerapp.model.ayahs


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

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