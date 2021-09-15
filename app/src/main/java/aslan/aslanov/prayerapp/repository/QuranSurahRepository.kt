package aslan.aslanov.prayerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.surahs.QuranResponse
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService.getQuranResponse
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.util.convertToSurahEntity
import aslan.aslanov.prayerapp.util.catchServerError
import aslan.aslanov.prayerapp.util.logApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuranSurahRepository(private val database: PrayerDatabase) {
    private val service = getQuranResponse


    private var _quranUiState = MutableLiveData<Boolean>()
    val quranUiState: LiveData<Boolean>
        get() = _quranUiState

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private suspend fun fetchQuran(onCompleteListener: (NetworkResult<QuranResponse>) -> Unit) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = service.fetchSurahFromQuran()
            if (res.isSuccessful) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error(res.message()))
            } else {
                catchServerError<QuranResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }

    suspend fun addQuranSurahToDB() {
        fetchQuran { res ->
            when (res.status) {
                Status.LOADING -> {
                    _quranUiState.value = true
                }
                Status.ERROR -> {
                    res.msg?.let {
                        logApp(res.msg)
                        _errorMessage.value = it
                        _quranUiState.value = false
                    }
                }
                Status.SUCCESS -> {
                    res.data?.let {
                        it.data!!.let { surahs ->
                            GlobalScope.launch {
                                val convertedSurah = withContext(Dispatchers.Default)
                                {
                                    return@withContext surahs.convertToSurahEntity()
                                }
                                database.getQuranDao().insertSurah(*convertedSurah.toTypedArray())
                            }
                            _quranUiState.postValue(false)
                        }
                    }
                }
            }
        }
    }

    fun getSurahFromDB(): LiveData<List<SurahEntity>> {
        _quranUiState.postValue(false)
        return (database.getQuranDao().getSurahsFromDatabase())
    }
}