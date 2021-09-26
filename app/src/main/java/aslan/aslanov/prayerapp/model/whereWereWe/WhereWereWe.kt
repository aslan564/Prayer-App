package aslan.aslanov.prayerapp.model.whereWereWe

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_where_were")
data class WhereWereWe(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    @ColumnInfo(name = "scrollPosition")
    val position: Int,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int,
    @ColumnInfo(name = "surahOrAyahs")
    val surahOrAyahs: String
)
enum class AyahsOrSurah{
    AYAHS,HADEETHS,PRAYER
}
