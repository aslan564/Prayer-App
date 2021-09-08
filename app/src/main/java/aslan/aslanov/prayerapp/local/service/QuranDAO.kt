package aslan.aslanov.prayerapp.local.service

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.language.QuranLanguage
import aslan.aslanov.prayerapp.model.surahs.SurahEntity

@Dao
interface QuranDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurah(vararg surahEntity: SurahEntity)

    @Query("SELECT * FROM table_surah")
    fun getSurahsFromDatabase(): LiveData<List<SurahEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuranLanguage(vararg language: QuranLanguage)

    @Query("SELECT * FROM table_language")
    fun getLanguageFromDatabase(): LiveData<QuranLanguage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuranAyah(vararg ayahs: AyahEntity)

    @Query("SELECT * FROM table_ayahs where surahId=:idSurah")
    fun getAyahsFromDatabase(idSurah: Int): LiveData<AyahEntity>
}