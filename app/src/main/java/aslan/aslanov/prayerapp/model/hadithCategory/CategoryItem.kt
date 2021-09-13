package aslan.aslanov.prayerapp.model.hadithCategory


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    @Json(name = "hadeeths_count")
    val hadeethsCount: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "parent_id")
    val parentId: String?,
    @Json(name = "title")
    val title: String?
):Parcelable

@Parcelize
@Entity(tableName = "table_category")
data class CategoryEntity(
    @ColumnInfo(name = "hadeeths_count")
    val hadeethsCount: String,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "parent_id")
    val parentId: String,
    @ColumnInfo(name = "title")
    val title: String
):Parcelable