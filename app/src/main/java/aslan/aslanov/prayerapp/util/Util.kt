package aslan.aslanov.prayerapp.util

enum class Prayers{
    ASR,
    IMSAK,
    MAGHRIB,
    SUNRISE,
    MIDNIGHT,
    FAJR,
    DHUHUR,
    ISHA,
    SUNSET,
}
object PendingRequests{
    const val REQUEST_CODE_PRAYER_TIME = 155
    const val REQUEST_CODE_HADEETHS = 133
    const val REQUEST_CODE_AYAHS = 199
    const val REQUEST_CODE_SALAWAT = 156
    const val CATCH_REQUEST_CODE_FROM_MAIN = "CATCH_REQUEST_CODE_FROM_MAIN"

}