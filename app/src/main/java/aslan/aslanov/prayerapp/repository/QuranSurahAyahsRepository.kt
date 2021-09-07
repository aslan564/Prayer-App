package aslan.aslanov.prayerapp.repository

import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.model.surahs.QuranResponse
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService.getQuranResponse
import aslan.aslanov.prayerapp.util.getServerError

class QuranSurahAyahsRepository {
    private val service = getQuranResponse

    suspend fun fetchSurahAyahsFromQuran(
        surah: Int,
        edition: String, onCompleteListener: (NetworkResult<AyahsResponse>) -> Unit
    ) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = service.fetchSurahAyahsQuran(surah,edition)
            if (res.isSuccessful && res.code() == 200) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error("data not loaded"))
            } else {
                getServerError<AyahsResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }
}