package aslan.aslanov.prayerapp.ui.fragment.calendar.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.prayerCurrent.Data
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter
import aslan.aslanov.prayerapp.util.GenericListAdapter

@SuppressLint("ResourceType")
class CalendarAdapter(
    newList: List<Data>,
    onLayoutClickListener: (ViewDataBinding, Data, List<Data>, Int) -> Unit
) :
    GenericRecyclerViewAdapter<Data>(newList,
        R.layout.layout_calendar_item,
        onCompleteListener = { viewDataBinding, data, list, i ->
            onLayoutClickListener(viewDataBinding, data, list, i)
        })