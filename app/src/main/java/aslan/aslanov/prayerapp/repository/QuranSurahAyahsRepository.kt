package aslan.aslanov.prayerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService.getQuranResponse
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.util.convertToAyah
import aslan.aslanov.prayerapp.util.catchServerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuranSurahAyahsRepository(private val database: PrayerDatabase) {
    private val service = getQuranResponse

    private var _baseErrorMessage = MutableLiveData<String>()
    val baseErrorMessage: LiveData<String>
        get() = _baseErrorMessage

    private var _baseLoadingStatus = MutableLiveData<Boolean>()
    val baseLoading: LiveData<Boolean>
        get() = _baseLoadingStatus


    private suspend fun fetchSurahAyahsFromQuran(
        surah: Int,
        edition: String, onCompleteListener: (NetworkResult<AyahsResponse>) -> Unit
    ) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = service.fetchSurahAyahsQuran(surah, edition)
            if (res.isSuccessful) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error(res.message()))
            } else {
                catchServerError<AyahsResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }
        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }


    suspend fun fetchSurahAyahs(
        surah: Int,
        edition: String
    ) {
        fetchSurahAyahsFromQuran(surah, edition) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    result.data?.let {
                        GlobalScope.launch {

                            withContext(Dispatchers.Default)
                            {
                                return@withContext it.data!!.ayahs?.let { ayahList ->
                                    it.data.number?.let { surahId ->
                                        ayahList.convertToAyah(
                                            surahId,
                                            it.data.name!!,
                                            it.data.englishName!!
                                        )
                                    }
                                }
                            }?.let { ayahEntity ->
                                launch {
                                    database.getQuranDao()
                                        .insertQuranAyah(*ayahEntity.toTypedArray())
                                }
                            }
                            _baseLoadingStatus.postValue(false)
                        }
                    }
                }
                Status.LOADING -> {
                    _baseLoadingStatus.postValue(true)
                }
                Status.ERROR -> {
                    result.msg?.let {
                        _baseLoadingStatus.postValue(false)
                        _baseErrorMessage.value = it
                    }
                }
            }
        }
    }

    fun getAyahsFromDatabase(surah: Int, onCompleteListener: (LiveData<List<AyahEntity>>) -> Unit) {
        onCompleteListener(database.getQuranDao().getAyahsFromDatabase(surah))
        _baseLoadingStatus.postValue(false)
    }

}