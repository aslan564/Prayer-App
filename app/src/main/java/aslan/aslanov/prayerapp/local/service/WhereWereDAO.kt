package aslan.aslanov.prayerapp.local.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe

@Dao
interface WhereWereDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWhereWere(vararg whereWereWe: WhereWereWe)

    @Query("SELECT * FROM table_where_were WHERE id=:incomeID")
    suspend fun getWhereWere(incomeID:Int):WhereWereWe
}