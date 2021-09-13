package aslan.aslanov.prayerapp.ui.fragment.hadeeths.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.hadeeths.Data
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class HadeethsAdapter(
    listHadeeths: List<HadeethsEntity>,
    onCompleteListener: (ViewDataBinding, HadeethsEntity, List<HadeethsEntity>, Int) -> Unit
) : GenericRecyclerViewAdapter<HadeethsEntity>(listHadeeths, R.layout.layout_item_quran_hadeeth,
    onCompleteListener = { viewDataBinding: ViewDataBinding, data: HadeethsEntity, list: List<HadeethsEntity>, i: Int ->
        onCompleteListener(viewDataBinding, data, list, i)
    })