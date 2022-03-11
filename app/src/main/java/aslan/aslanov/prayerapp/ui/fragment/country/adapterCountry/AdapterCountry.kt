package aslan.aslanov.prayerapp.ui.fragment.country.adapterCountry

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.util.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class AdapterCountry(newList:List<CountryWithCities>,onCompletionListener: (ViewDataBinding, List<CountryWithCities>, Int,CountryWithCities) -> Unit) :
    GenericRecyclerViewAdapter<CountryWithCities>(
        newList,
        R.layout.layout_item_country,
        onCompleteListener = { viewDataBinding: ViewDataBinding, item:CountryWithCities, list: List<CountryWithCities>, position: Int ->
            onCompletionListener(viewDataBinding, list, position,item)
        }
    )

