package aslan.aslanov.prayerapp.model.language


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Data(
    @Json(name = "direction")
    val direction: String?,
    @Json(name = "englishName")
    val englishName: String?,
    @Json(name = "format")
    val format: String?,
    @Json(name = "identifier")
    val identifier: String?,
    @Json(name = "language")
    val language: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "type")
    val type: String?
)

@Entity(tableName = "table_language")
data class QuranLanguage(
    @ColumnInfo(name = "direction")
    val direction: String,
    @ColumnInfo(name = "englishName")
    val englishName: String,
    @ColumnInfo(name = "format")
    val format: String,
    @PrimaryKey(autoGenerate = false)
    val identifier: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: String,


)