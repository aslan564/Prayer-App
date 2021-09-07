package aslan.aslanov.prayerapp.repository

import aslan.aslanov.prayerapp.model.surahs.QuranResponse
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService.getQuranResponse
import aslan.aslanov.prayerapp.util.getServerError

class QuranSurahRepository {
    private val service = getQuranResponse

    suspend fun fetchQuran(onCompleteListener: (NetworkResult<QuranResponse>)->Unit) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = service.fetchSurahFromQuran()
            if (res.isSuccessful && res.code() == 200) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                }?:onCompleteListener(NetworkResult.error("data not loaded"))
            }else{
                getServerError<QuranResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }
}