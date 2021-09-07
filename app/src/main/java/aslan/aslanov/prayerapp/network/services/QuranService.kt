package aslan.aslanov.prayerapp.network.services

import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.model.surahs.QuranResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranService {

    @GET("edition/type/translation")
    suspend fun fetchQuranTranslations()

    @GET("surah/{surah}/{edition}")
    suspend fun fetchSurahAyahsQuran(
        @Path(value = "surah") surah: Int,
        @Path(value = "edition") edition: String
    ): Response<AyahsResponse>


    @GET("surah")
    suspend fun fetchSurahFromQuran(
    ): Response<QuranResponse>
}