package aslan.aslanov.prayerapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageHadeeth
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsResponse
import aslan.aslanov.prayerapp.model.hadeeths.convertToHadeeths
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryEntity
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryItem
import aslan.aslanov.prayerapp.model.language.LanguageResponse
import aslan.aslanov.prayerapp.model.language.category.HadithLanguageItem
import aslan.aslanov.prayerapp.network.NetworkResult
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.network.RetrofitService.getHadeethsService
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.util.convertToCategoryEntity
import aslan.aslanov.prayerapp.util.getServerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HadithAndLanguageRepository(private val database: PrayerDatabase) {
    private val service = getHadeethsService


    private var _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


    suspend fun getQuranLanguage(onCompleteListener: (NetworkResult<LanguageResponse>) -> Unit) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = RetrofitService.getQuranResponse.fetchQuranTranslationsLanguage()
            if (res.isSuccessful) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error("Language data not loaded"))
            } else {
                getServerError<LanguageResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }

    suspend fun getHadeethLanguage(onCompleteListener: (NetworkResult<List<HadithLanguageItem>>) -> Unit) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = RetrofitService.getHadeethsService.getHadithLanguage()
            if (res.isSuccessful) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error("Language data not loaded"))
            } else {
                getServerError<List<HadithLanguageItem>>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }

        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }


    private suspend fun getCategoryList(
        language: String,
        onCompleteListener: (NetworkResult<List<CategoryItem>>) -> Unit
    ) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = service.getHadithCategory(language)
            if (res.isSuccessful) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error("data not loaded please retry"))
            } else {
                getServerError<List<CategoryItem>>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }
        } catch (e: java.lang.Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }

    suspend fun addCategoryToDatabase() {
        if (languageHadeeth != null) {
            getCategoryList(languageHadeeth!!) { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        res.data?.let {
                            GlobalScope.launch {
                                try {
                                    database.getHadeeth().deleteAllCategory()
                                    val converted = withContext(Dispatchers.Default) {
                                        return@withContext it.convertToCategoryEntity()
                                    }
                                    database.getHadeeth().insertCategory(*converted.toTypedArray())
                                    _loading.postValue(false)
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                }
                            }

                        }
                    }
                    Status.LOADING -> {
                        _loading.postValue(true)
                    }
                    Status.ERROR -> {
                        res.msg?.let {
                            _loading.postValue(false)
                            _error.postValue(it)
                        }
                    }
                }

            }
        }

    }

    private suspend fun getHadith(
        language: String,
        categoryId: Int,
        page: Int,
        perPage: Int,
        onCompleteListener: (NetworkResult<HadeethsResponse>) -> Unit
    ) {
        try {
            onCompleteListener(NetworkResult.loading())
            val res = service.getHadithFromCategory(language, categoryId, page, perPage)
            if (res.isSuccessful) {
                res.body()?.let {
                    onCompleteListener(NetworkResult.success(it))
                } ?: onCompleteListener(NetworkResult.error("data not loaded please retry"))
            } else {
                getServerError<HadeethsResponse>(res.errorBody()) {
                    onCompleteListener(it)
                }
            }
        } catch (e: Exception) {
            onCompleteListener(NetworkResult.error(e.message))
        }
    }

    suspend fun clearHadeethsFromDb(categoryId: Int) {
        database.getHadeeth().deleteAllHadeeths(categoryId)
    }

    suspend fun addHadithToDatabase(
        categoryId: Int,
        page: Int,
        perPage: Int,
        hadeethsName: String,
    ) {
        if (languageHadeeth != null) {
            getHadith(languageHadeeth!!, categoryId, page, perPage) { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        res.data?.let {
                            GlobalScope.launch {
                                try {
                                    it.data?.let { dataList ->
                                        val convertList = withContext(Dispatchers.Default) {
                                            return@withContext dataList.convertToHadeeths(
                                                languageHadeeth!!,
                                                categoryId,
                                                hadeethsName
                                            )
                                        }
                                        database.getHadeeth()
                                            .insertHadeethsByCategory(*convertList.toTypedArray())
                                    }
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                }
                            }
                            _loading.postValue(false)
                        }
                    }
                    Status.LOADING -> {
                        _loading.postValue(true)
                    }
                    Status.ERROR -> {
                        res.msg?.let {
                            _loading.postValue(false)
                            _error.postValue(it)
                        }
                    }
                }
            }
        }
    }

    fun getHadithFromDb(categoryId: Int): LiveData<List<HadeethsEntity>> {
        return database.getHadeeth().getHadeethsByCategory(categoryId)
    }

    fun getCategoryOfHadeeths(): LiveData<List<CategoryEntity>> {
        _loading.postValue(false)
        return database.getHadeeth().getCategoryOfHadeeths()
    }

    companion object {
        private const val TAG = "HadithAndLanguageRepo"
    }
}