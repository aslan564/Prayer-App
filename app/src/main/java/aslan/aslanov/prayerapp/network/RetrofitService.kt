package aslan.aslanov.prayerapp.network

import aslan.aslanov.prayerapp.BuildConfig
import aslan.aslanov.prayerapp.network.RetrofitFactory.createService
import aslan.aslanov.prayerapp.network.services.CalendarService
import aslan.aslanov.prayerapp.network.services.CountryService
import aslan.aslanov.prayerapp.network.services.QuranService
import aslan.aslanov.prayerapp.util.NetworkConstant.BASE_URL
import aslan.aslanov.prayerapp.util.NetworkConstant.BASE_URL_COUNTRY
import aslan.aslanov.prayerapp.util.NetworkConstant.BASE_URL_QURAN

object RetrofitService {
    val getPrayerTimeForMonth by lazy { createService<CalendarService>(BuildConfig.DEBUG,BASE_URL) }
    val getCountryList by lazy { createService<CountryService>(BuildConfig.DEBUG, BASE_URL_COUNTRY) }
    val getQuranResponse by lazy { createService<QuranService>(BuildConfig.DEBUG, BASE_URL_QURAN) }
}