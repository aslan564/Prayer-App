package aslan.aslanov.prayerapp.util

object NetworkConstant {
    //qibla direction http://api.aladhan.com/v1/qibla/25.4106386/51.1846025
    //https://hadeethenc.com/api/v1/hadeeths/list/?language=tr&category_id=1&page=1&per_page=50
    const val BASE_URL="http://api.aladhan.com/v1/"
    const val BASE_URL_COUNTRY="https://countriesnow.space/api/v0.1/"
    const val BASE_URL_QURAN="http://api.alquran.cloud/v1/"
    const val BASE_URL_HADEETHS="https://hadeethenc.com/api/v1/"
}
object AppConstant {
    const val NOTIFICATION_ID="aslanaslanovprayerapputil"
    const val NOTIFICATION_MANAGER_AYAH_ID=123
    const val NOTIFICATION_MANAGER_HADEETHS_ID=223
    const val NOTIFICATION_MANAGER_SALAWAT_ID=325
    const val NOTIFICATION_MANAGER_PRAYER_ID=426
}