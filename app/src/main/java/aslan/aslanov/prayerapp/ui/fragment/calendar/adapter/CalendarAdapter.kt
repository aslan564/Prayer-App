package aslan.aslanov.prayerapp.ui.fragment.calendar.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.prayerCurrent.Data
import aslan.aslanov.prayerapp.util.GenericListAdapter

@SuppressLint("ResourceType")
class CalendarAdapter(onLayoutClickListener: (ViewDataBinding, Data) -> Unit) :
    GenericListAdapter<Data>(
        R.layout.layout_calendar_item,
        onCompleteListener = { viewDataBinding: ViewDataBinding, data: Data ->
            onLayoutClickListener(viewDataBinding, data)
        })