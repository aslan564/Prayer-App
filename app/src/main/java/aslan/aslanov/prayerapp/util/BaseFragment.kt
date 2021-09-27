package aslan.aslanov.prayerapp.util

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

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