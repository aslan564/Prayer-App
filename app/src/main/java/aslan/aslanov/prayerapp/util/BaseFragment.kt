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
abstract class BaseFragment(@IdRes val layoutID: Int) : Fragment(layoutID) {
    private var _binding: ViewDataBinding? = null

    private val binding: ViewDataBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutID, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI(binding)
        observeData()
    }

    protected open fun bindUI(binding: ViewDataBinding): Unit = with(this.binding) {
        lifecycleOwner = this@BaseFragment
    }
    abstract fun observeData()
}