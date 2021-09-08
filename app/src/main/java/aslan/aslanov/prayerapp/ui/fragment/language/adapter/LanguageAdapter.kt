package aslan.aslanov.prayerapp.ui.fragment.language.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.language.Data
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class LanguageAdapter(private val listLanguage:List<Data>, onCompleteListener: (ViewDataBinding,Data,List<Data>,Int)->Unit):GenericRecyclerViewAdapter<Data>(listLanguage,
    R.layout.layout_item_quran_language,onClickListener = {viewDataBinding, data, list, i ->
        onCompleteListener(viewDataBinding,data,list,i)
    })