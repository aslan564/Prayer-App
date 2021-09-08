package aslan.aslanov.prayerapp.repository

import aslan.aslanov.prayerapp.model.language.LanguageResponse
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.util.getServerError

class QuranLanguageRepository {
    private val service = RetrofitService.getQuranResponse

    suspend fun getQuranLanguage(onCompleteListener: (NetworkResult<LanguageResponse>) -> Unit) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = service.fetchQuranTranslationsLanguage()
            if (res.isSuccessful && res.code() == 200) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                }?:onCompleteListener(NetworkResult.error("Language data not loaded"))
            }else{
                getServerError<LanguageResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }
}