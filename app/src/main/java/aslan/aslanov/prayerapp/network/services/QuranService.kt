package aslan.aslanov.prayerapp.network.services

import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.model.language.LanguageResponse
import aslan.aslanov.prayerapp.model.newQuranModel.QuranResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranService {

    @GET("edition")
    suspend fun fetchQuranTranslationsLanguage():Response<LanguageResponse>

    @GET("surah/{surah}/{edition}")
    suspend fun fetchSurahAyahsQuran(
        @Path(value = "surah") surah: Int,
        @Path(value = "edition") edition: String
    ): Response<AyahsResponse>

    @GET("surah")
    suspend fun getQuran(
    ): Response<QuranResponse>
}