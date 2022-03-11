package aslan.aslanov.prayerapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.network.RetrofitService.getQuranResponse
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.util.convertToAyah
import aslan.aslanov.prayerapp.util.catchServerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.lang.Exception

class QuranSurahAyahsRepository(
    private val database: PrayerDatabase,
    private val context: Context,
    private val retrofit: RetrofitService
) {
    suspend fun getAyahsFromDatabase(surah: Int) =
        database.getQuranDao().getAyahsFromDatabase(surah)
}