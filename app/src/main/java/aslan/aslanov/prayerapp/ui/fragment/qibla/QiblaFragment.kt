package aslan.aslanov.prayerapp.ui.fragment.qibla

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentQiblaBinding
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.BaseFragment


@SuppressLint("ResourceType")
class QiblaFragment : BaseFragment() {

    private val bindingFragment by lazy { FragmentQiblaBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }

    override fun bindUI():Unit= with(bindingFragment) {

    }

    override fun observeData() {

    }


    companion object {

    }
}