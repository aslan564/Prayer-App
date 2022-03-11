package aslan.aslanov.prayerapp.repository

import android.content.Context
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.model.newQuranModel.Ayah
import aslan.aslanov.prayerapp.model.newQuranModel.QuranResponse
import aslan.aslanov.prayerapp.model.newQuranModel.Surah
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.util.catchServerError
import aslan.aslanov.prayerapp.util.convertToAyah
import aslan.aslanov.prayerapp.util.convertToSurahEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuranSurahRepository(
    private val database: PrayerDatabase,
    private val context: Context,
    private val retrofit: RetrofitService
) {

    suspend fun addQuranSurahToDB(surahs: List<Surah>) {
        val convertedSurah = withContext(Dispatchers.Default)
        {
            return@withContext surahs.convertToSurahEntity()
        }
        database.getQuranDao().insertSurah(*convertedSurah.toTypedArray())
    }

    suspend fun addQuranAyahsToDB(surahs: SurahEntity, ayahs: List<Ayah>) {
        withContext(Dispatchers.Default)
        {
            return@withContext surahs.number.let { surah ->
                ayahs.convertToAyah(
                    surah,
                    surahs.name,
                    surahs.englishName
                )
            }
        }?.let { ayahEntity ->
            database.getQuranDao()
                .insertQuranAyah(*ayahEntity.toTypedArray())
        }
    }

    suspend fun getSurahFromDB() = database.getQuranDao().getSurahsFromDatabase()


    suspend fun fetchNewQuran(
        onCompleteListener: (NetworkResult<QuranResponse>) -> Unit
    ) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = retrofit.getQuranResponse.getQuran()
            if (res.isSuccessful) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error(res.message()))
            } else {
                catchServerError<QuranResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }

    suspend fun getAyahFromInternet(
        surah: Int,
        language: String,
        onComplete: (NetworkResult<AyahsResponse>) -> Unit
    ) {
        try {
            val response = retrofit.getQuranResponse.fetchSurahAyahsQuran(surah, language)
            if (response.isSuccessful) {
                response.body()?.let {
                    onComplete(NetworkResult.success(it))
                } ?: onComplete(NetworkResult.error(response.message()))
            } else {
                catchServerError<AyahsResponse>(response.errorBody()) {
                    onComplete(it)
                }
            }
        } catch (e: java.lang.Exception) {
            onComplete(NetworkResult.error(e.message))
        }
    }

    fun getAyahsFromDatabase(surah: Int) =
        database.getQuranDao().getAyahsFromDatabase(surah)
}