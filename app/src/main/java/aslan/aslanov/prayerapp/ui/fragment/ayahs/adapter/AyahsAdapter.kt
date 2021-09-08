package aslan.aslanov.prayerapp.ui.fragment.ayahs.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.ayahs.Ayah
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class AyahsAdapter(
    private val ayahList: List<Ayah>,
    onClickAyahListener: (ViewDataBinding, Ayah, List<Ayah>, Int) -> Unit
) : GenericRecyclerViewAdapter<Ayah>(
    ayahList,
    R.layout.layout_item_quran_ayahs,
    onClickListener = { viewDataBinding, ayah, list, i ->
        onClickAyahListener(viewDataBinding, ayah, list, i)

    })