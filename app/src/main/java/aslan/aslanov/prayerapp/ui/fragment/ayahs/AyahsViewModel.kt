package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.ayahs.AyahsResponse
import aslan.aslanov.prayerapp.model.baseViewModel.BaseViewModel
import aslan.aslanov.prayerapp.model.newQuranModel.Ayah
import aslan.aslanov.prayerapp.model.newQuranModel.Surah
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.network.Status
import aslan.aslanov.prayerapp.repository.QuranSurahAyahsRepository
import aslan.aslanov.prayerapp.repository.QuranSurahRepository
import aslan.aslanov.prayerapp.repository.WhereWereRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class AyahsViewModel(
    database: PrayerDatabase,
    context: Context, retrofit: RetrofitService
) : BaseViewModel<WhereWereWe>() {
    private val repository by lazy { QuranSurahRepository(database, context, retrofit) }
    private val repositoryWhereWereWe by lazy { WhereWereRepository(database) }

    fun fetchSurahAyahs(
        surah: Int,
    ) = repository.getAyahsFromDatabase(surah)


    fun setWhereWee(whereWereWe: WhereWereWe) = viewModelScope.launch {
        repositoryWhereWereWe.setPositionFromDatabase(whereWereWe)
    }

    fun getWhereWee(id: String) = viewModelScope.launch {
        setData(repositoryWhereWereWe.getPositionFromDatabase(id)) {
            setLoading(false)
        }
    }


}