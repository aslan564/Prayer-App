package aslan.aslanov.prayerapp.ui.fragment.surah.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class QuranSurahAdapter(
    newList: List<SurahEntity>,
    onCompletionListener: (ViewDataBinding, List<SurahEntity>, Int, SurahEntity) -> Unit
) : GenericRecyclerViewAdapter<SurahEntity>(
    newList,
    R.layout.layout_item_quran_surahs,
    onCompleteListener = { viewDataBinding: ViewDataBinding, item: SurahEntity, list: List<SurahEntity>, position: Int ->
        onCompletionListener(viewDataBinding, list, position, item)
    }
)