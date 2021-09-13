package aslan.aslanov.prayerapp.network.services

import aslan.aslanov.prayerapp.model.hadeeths.HadeethsResponse
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryItem
import aslan.aslanov.prayerapp.model.language.category.HadithLanguageItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HadeethsService {

    //categories/roots/?language=en
    @GET("hadeeths/list/")
    suspend fun getHadithFromCategory(
        @Query(value = "language") language: String="en",
        @Query(value = "category_id") categoryId: Int,
        @Query(value = "page") page: Int=1,
        @Query(value = "per_page") per_page: Int = 20,
    ):Response<HadeethsResponse>

    @GET("categories/list/")
    suspend fun getHadithCategory(
        @Query(value = "language") language: String="en"
    ):Response<List<CategoryItem>>

    @GET("languages")
    suspend fun getHadithLanguage(
    ):Response<List<HadithLanguageItem>>
}