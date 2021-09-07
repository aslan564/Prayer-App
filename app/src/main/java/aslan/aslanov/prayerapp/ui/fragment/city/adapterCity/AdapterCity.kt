package aslan.aslanov.prayerapp.ui.fragment.city.adapterCity

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.countryModel.City
import aslan.aslanov.prayerapp.util.GenericListAdapter

@SuppressLint("ResourceType")
class AdapterCity(private val onClickListener: (ViewDataBinding,City) -> Unit) :
    GenericListAdapter<City>(R.layout.layout_item_city,onCompleteListener = {viewDataBinding: ViewDataBinding, city: City ->
        onClickListener(viewDataBinding,city)

    })