package aslan.aslanov.prayerapp.util

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

@SuppressLint("ResourceType")
abstract class BaseFragment() : Fragment() {
    private var binding: ViewDataBinding? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeData()
    }

    protected open fun bindUI(): Unit = with(this.binding) {
        this?.lifecycleOwner = this@BaseFragment
    }
    abstract fun observeData()
}