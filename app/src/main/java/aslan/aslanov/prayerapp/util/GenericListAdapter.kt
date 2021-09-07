package aslan.aslanov.prayerapp.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class GenericListAdapter<T : Any>(
    @IdRes val layoutId: Int,
    private val onCompleteListener: (ViewDataBinding, T) -> Unit
) :
    ListAdapter<T, BaseViewHolder<T>>(BaseItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return BaseViewHolder.from(parent, layoutId)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(getItem(position), onCompleteListener)
    }
}

class BaseItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}

class BaseViewHolder<T : Any>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: T,
        onCompleteListenerView: (ViewDataBinding, T) -> Unit
    ): Unit = with(binding) {
        onCompleteListenerView(binding, item)
        binding.executePendingBindings()
    }

    companion object {
        fun <T : Any> from(parent: ViewGroup, layoutId: Int): BaseViewHolder<T> {
            val inflater = LayoutInflater.from(parent.context)
            val root = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
            return BaseViewHolder(root)
        }
    }

}
