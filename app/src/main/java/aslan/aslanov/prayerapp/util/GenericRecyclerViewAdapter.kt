package aslan.aslanov.prayerapp.ui.fragment.city.adapterCity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("ResourceType")
open class GenericRecyclerViewAdapter<T : Any>(
    private var cityWithCities: List<T>,
    @IdRes val layoutId: Int,
    private val onClickListener: (ViewDataBinding,T, List<T>, Int) -> Unit
) : RecyclerView.Adapter<ViewHolderGeneric<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneric<T> {
        return ViewHolderGeneric.from(parent, layoutId)
    }


    override fun onBindViewHolder(holder: ViewHolderGeneric<T>, position: Int) {
        holder.bind(cityWithCities[position],cityWithCities, position, onClickListener)
    }

    override fun getItemCount(): Int {
        return cityWithCities.size
    }
}

class ViewHolderGeneric<T : Any> private constructor(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: T,
        itemList: List<T>,
        position: Int,
        onCompletionListener: (ViewDataBinding,T, List<T>, Int) -> Unit
    ): Unit = with(binding) {
        onCompletionListener(binding,item, itemList, position)
        executePendingBindings()
    }

    companion object {
        fun <T : Any> from(parent: ViewGroup, layoutId: Int): ViewHolderGeneric<T> {
            val inflater = LayoutInflater.from(parent.context)
            val view = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
            return ViewHolderGeneric(view)
        }
    }
}