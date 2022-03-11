package aslan.aslanov.prayerapp.local.manager

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreferenceManager {
    private const val SHARED_PREFERENCE_KEY = "aslan.aslanov.prayerapp.local.manager"

    private lateinit var sharedPreferences: SharedPreferences

    private val IS_ASR = Pair("isAsrPrayer", false)
    private val IS_ISHA = Pair("isIshaPrayer", false)
    private val IS_DHUHR = Pair("isDhuhrPrayer", false)
    private val IS_FAJR = Pair("isFajrPrayer", false)
    private val IS_IMSAK = Pair("isImsakPrayer", false)
    private val IS_MAGHRIB = Pair("isMaghribPrayer", false)
    private val IS_MIDNIGHT = Pair("isMidnightPrayer", false)
    private val IS_SUNRISE = Pair("isSunrisePrayer", false)
    private val IS_SUNSET = Pair("isSunsetPrayer", false)


    private val IS_SURAH_FIRST = Pair("isSurahFirst", false)
    private val IS_LOCATION = Pair("isLocation", true)
    private val IS_FIRST_TIME = Pair("isFirstTime", true)
    private val LATITUDE = Pair<String, String?>("latitude", null)
    private val LONGITUDE = Pair<String, String?>("longitude", null)
    private val CITY_NAME = Pair<String, String?>("location", null)
    private val COUNTRY_NAME = Pair<String, String?>("country_name", null)
    private val QURAN_LANGUAGE_SURAH = Pair("quran_language_surah", "en.asad")
    private val QURAN_LANGUAGE_HADEETH = Pair<String, String?>("quran_language_hadeeth", null)

    fun instance(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_KEY, MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }


    var isLatitude: String?
        get() = sharedPreferences.getString(LATITUDE.first, LATITUDE.second)
        set(value) = sharedPreferences.edit {
            it.putString(LATITUDE.first, value)
        }
    var isLongitude: String?
        get() = sharedPreferences.getString(LONGITUDE.first, LONGITUDE.second)
        set(value) = sharedPreferences.edit {
            it.putString(LONGITUDE.first, value)
        }

    var locationCityName: String?
        get() = sharedPreferences.getString(CITY_NAME.first, CITY_NAME.second)
        set(value) = sharedPreferences.edit {
            it.putString(CITY_NAME.first, value)
        }
    var locationCountryName: String?
        get() = sharedPreferences.getString(COUNTRY_NAME.first, COUNTRY_NAME.second)
        set(value) = sharedPreferences.edit {
            it.putString(COUNTRY_NAME.first, value)
        }
    var languageSurah: String?
        get() = sharedPreferences.getString(QURAN_LANGUAGE_SURAH.first, QURAN_LANGUAGE_SURAH.second)
        set(value) = sharedPreferences.edit {
            it.putString(QURAN_LANGUAGE_SURAH.first, value)
        }
    var languageHadeth: String?
        get() = sharedPreferences.getString(
            QURAN_LANGUAGE_HADEETH.first,
            QURAN_LANGUAGE_HADEETH.second
        )
        set(value) = sharedPreferences.edit {
            it.putString(QURAN_LANGUAGE_HADEETH.first, value)
        }
    var isFirstTime: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_TIME.first, IS_FIRST_TIME.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_FIRST_TIME.first, value)
        }
    var isLocation: Boolean
        get() = sharedPreferences.getBoolean(IS_LOCATION.first, IS_LOCATION.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_LOCATION.first, value)
        }
    var isSurahFirst: Boolean
        get() = sharedPreferences.getBoolean(IS_SURAH_FIRST.first, IS_SURAH_FIRST.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_SURAH_FIRST.first, value)
        }


    //Notification checker
    var isAsr: Boolean
        get() = sharedPreferences.getBoolean(IS_ASR.first, IS_ASR.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_ASR.first, value)
        }
    var isDhuhur: Boolean
        get() = sharedPreferences.getBoolean(IS_DHUHR.first, IS_DHUHR.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_DHUHR.first, value)
        }
    var isMaghrib: Boolean
        get() = sharedPreferences.getBoolean(IS_MAGHRIB.first, IS_MAGHRIB.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_MAGHRIB.first, value)
        }
    var isFajr: Boolean
        get() = sharedPreferences.getBoolean(IS_FAJR.first, IS_FAJR.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_FAJR.first, value)
        }
    var isIsha: Boolean
        get() = sharedPreferences.getBoolean(IS_ISHA.first, IS_ISHA.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_ISHA.first, value)
        }
    var isImsak: Boolean
        get() = sharedPreferences.getBoolean(IS_IMSAK.first, IS_IMSAK.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_IMSAK.first, value)
        }
    var isMidnight: Boolean
        get() = sharedPreferences.getBoolean(IS_MIDNIGHT.first, IS_MIDNIGHT.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_MIDNIGHT.first, value)
        }

    var isSunrise: Boolean
        get() = sharedPreferences.getBoolean(IS_SUNRISE.first, IS_SUNRISE.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_SUNRISE.first, value)
        }
    var isSunset: Boolean
        get() = sharedPreferences.getBoolean(IS_SUNSET.first, IS_SUNSET.second)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_SUNSET.first, value)
        }
}