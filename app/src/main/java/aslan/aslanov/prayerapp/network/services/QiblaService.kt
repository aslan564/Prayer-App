package aslan.aslanov.prayerapp.network.services

import retrofit2.http.GET
import retrofit2.http.Path

interface QiblaService {
    @GET("qibla/{latitude}/{longitude}")
    suspend fun getQiblaLonAndLat(
        @Path(value = "latitude")latitude:Float,
        @Path(value = "longitude")longitude:Float,
    )
}