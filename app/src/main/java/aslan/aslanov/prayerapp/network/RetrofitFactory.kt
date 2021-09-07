package aslan.aslanov.prayerapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitFactory {

    inline fun <reified T> createService(debug: Boolean, baseUrl: String): T {
        var retrofit: T? = null
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val httpClient = OkHttpClient.Builder()
        if (debug) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .build()
                .create(T::class.java)
        }
        return retrofit as T
    }

}