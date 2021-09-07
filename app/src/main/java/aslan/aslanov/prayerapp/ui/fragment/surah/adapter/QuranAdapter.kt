package aslan.aslanov.prayerapp.ui.fragment.surah.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.surahs.Data
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class QuranAdapter(
    newList: List<Data>,
    onCompletionListener: (ViewDataBinding, List<Data>, Int, Data) -> Unit
) : GenericRecyclerViewAdapter<Data>(
    newList,
    R.layout.layout_item_quran,
    onClickListener = { viewDataBinding: ViewDataBinding, item: Data, list: List<Data>, position: Int ->
        onCompletionListener(viewDataBinding, list, position, item)
    }
)