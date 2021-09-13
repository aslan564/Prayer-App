package aslan.aslanov.prayerapp.ui.fragment.categoryOfHadeeth.adapter

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.hadeeths.Data
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryEntity
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryItem
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.GenericRecyclerViewAdapter

@SuppressLint("ResourceType")
class HadithCategoryAdapter(
    listHadeeths: List<CategoryEntity>,
    onCompleteListener: (ViewDataBinding, CategoryEntity, List<CategoryEntity>, Int) -> Unit
) : GenericRecyclerViewAdapter<CategoryEntity>(listHadeeths,
    R.layout.layout_item_quran_hadeeth_category,
    onCompleteListener = { viewDataBinding: ViewDataBinding, categoryEntity: CategoryEntity, list: List<CategoryEntity>, i: Int ->
        onCompleteListener(viewDataBinding, categoryEntity, list, i)
    })
