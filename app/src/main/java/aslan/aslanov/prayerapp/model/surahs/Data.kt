package aslan.aslanov.prayerapp.model.surahs


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import aslan.aslanov.prayerapp.model.newQuranModel.Ayah
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

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
    @ColumnInfo(name = "revelationType")
    val revelationType: String
):Parcelable