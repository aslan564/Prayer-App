package aslan.aslanov.prayerapp.network.services

import aslan.aslanov.prayerapp.model.countryModel.CountryResponse
import retrofit2.Response
import retrofit2.http.GET

interface CountryService {

    @GET("countries")
    suspend fun getAllCountry(): Response<CountryResponse>
}