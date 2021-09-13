package aslan.aslanov.prayerapp.ui.fragment.ayahs.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.ayahs.Ayah
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class AyahsAdapter(
    private val ayahList: List<AyahEntity>,
    onClickAyahListener: (ViewDataBinding, AyahEntity, List<AyahEntity>, Int) -> Unit
) : GenericRecyclerViewAdapter<AyahEntity>(
    ayahList,
    R.layout.layout_item_quran_ayahs,
    onCompleteListener = { viewDataBinding, ayah, list, i ->
        onClickAyahListener(viewDataBinding, ayah, list, i)

    })