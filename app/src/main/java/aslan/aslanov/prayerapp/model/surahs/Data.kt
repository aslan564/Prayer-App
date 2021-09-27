package aslan.aslanov.prayerapp.model.surahs


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class Data(
    @Json(name = "englishName")
    val englishName: String?,
    @Json(name = "englishNameTranslation")
    val englishNameTranslation: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "number")
    val number: Int?,
    @Json(name = "numberOfAyahs")
    val numberOfAyahs: Int?,
    @Json(name = "revelationType")
    val revelationType: String?
)
@Parcelize
@Entity(tableName = "table_surah")
data class SurahEntity(
    @ColumnInfo(name = "englishName")
    val englishName: String,
    @ColumnInfo(name = "englishNameTranslation")
    val englishNameTranslation: String,
    @ColumnInfo(name = "name")
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val number: Int,
    @ColumnInfo(name = "numberOfAyahs")
    val numberOfAyahs: Int,
    @ColumnInfo(name = "revelationType")
    val revelationType: String
):Parcelable