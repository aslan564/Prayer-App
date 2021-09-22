package aslan.aslanov.prayerapp.ui.fragment.timings.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsConverted
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class CurrentTimeAdapter(
    listConverted: List<TimingsConverted>,
    onCompleteCurrentTimeListener: (ViewDataBinding, TimingsConverted, List<TimingsConverted>, Int) -> Unit
) : GenericRecyclerViewAdapter<TimingsConverted>(listConverted,
    R.layout.layout_item_current_time,
    onCompleteListener = { viewDataBinding, timingsConverted, list, i ->
        onCompleteCurrentTimeListener(viewDataBinding, timingsConverted, list, i)
    })