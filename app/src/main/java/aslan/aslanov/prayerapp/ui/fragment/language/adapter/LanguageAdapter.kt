package aslan.aslanov.prayerapp.ui.fragment.language.adapter

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.util.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class LanguageAdapter<T:Any>(listLanguage:List<T>, @IdRes layoutId:Int, onCompleteListener: (ViewDataBinding, T, List<T>, Int)->Unit):
    GenericRecyclerViewAdapter<T>(listLanguage,
    layoutId, onCompleteListener = { viewDataBinding, data, list, i ->
        onCompleteListener(viewDataBinding,data,list,i)
    })