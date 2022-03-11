package aslan.aslanov.prayerapp.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.prayerapp.R

@SuppressLint("ResourceType")
open class GenericRecyclerViewAdapter<T : Any>(
    private var newList: List<T>,
    @IdRes val layoutId: Int,
    private val onCompleteListener: (ViewDataBinding, T, List<T>, Int) -> Unit
) : RecyclerView.Adapter<ViewHolderGeneric<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneric<T> {
        return ViewHolderGeneric.from(parent, layoutId)
    }


    override fun onBindViewHolder(holder: ViewHolderGeneric<T>, position: Int) {
        holder.bind(newList[position], newList, position, onCompleteListener)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderGeneric<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation();
    }

    override fun getItemCount(): Int {
        return newList.size
    }
}

class ViewHolderGeneric<T : Any> private constructor(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var lastAnimationPosition = -1

    fun bind(
        item: T,
        itemList: List<T>,
        position: Int,
        onCompletionListener: (ViewDataBinding, T, List<T>, Int) -> Unit
    ): Unit = with(binding) {
        onCompletionListener(binding, item, itemList, position)
        val id = if (position > lastAnimationPosition) {
            R.anim.up_from_bottom
        } else {
            R.anim.down_from_top
        }
        val animation = AnimationUtils.loadAnimation(root.context, id)
        binding.root.startAnimation(animation)
        lastAnimationPosition=position

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