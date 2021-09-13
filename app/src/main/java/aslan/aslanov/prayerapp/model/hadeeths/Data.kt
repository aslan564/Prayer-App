package aslan.aslanov.prayerapp.model.hadeeths


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Data(
    @Json(name = "id")
    val id: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "translations")
    val translations: List<String>?
)

@Entity(tableName = "table_hadeeths")
data class HadeethsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "categoryName")
    val categoryName: String,
    @ColumnInfo(name = "translation")
    val translations: String,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int
)

fun List<Data>.convertToHadeeths(
    translations: String,
    idCategory: Int,
    nameHadeeths: String,
): List<HadeethsEntity> = map {
    HadeethsEntity(
        id = it.id!!,
        title = it.title ?: "",
        translations = translations,
        categoryId=idCategory,
        categoryName = nameHadeeths
    )
}
