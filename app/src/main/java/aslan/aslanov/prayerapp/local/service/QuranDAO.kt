package aslan.aslanov.prayerapp.local.service

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.surahs.SurahEntity

@Dao
interface QuranDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurah(vararg surahEntity: SurahEntity)

    @Query("SELECT * FROM table_surah")
    suspend fun getSurahsFromDatabase(): List<SurahEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuranAyah(vararg ayahs: AyahEntity)

    @Query("DELETE FROM table_ayahs")
    suspend fun deleteAllAyahs()

    @Query("SELECT * FROM table_ayahs where surahId=:idSurah")
    fun getAyahsFromDatabase(idSurah: Int): LiveData<List<AyahEntity>>

    @Query("SELECT * FROM table_ayahs")
    fun getRandomAyahsFromDatabase(): LiveData<List<AyahEntity>>

    @Query("SELECT * FROM table_ayahs")
    suspend fun getRandomAyahsListFromDatabase(): List<AyahEntity>
}