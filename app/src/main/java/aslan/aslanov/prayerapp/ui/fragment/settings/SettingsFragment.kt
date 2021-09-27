package aslan.aslanov.prayerapp.ui.fragment.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentSettingsBinding
import aslan.aslanov.prayerapp.util.BaseFragment


@SuppressLint("ResourceType")
class SettingsFragment : BaseFragment() {
    private val bindingFragment by lazy { FragmentSettingsBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
        bindUI()
    }

    override fun bindUI(): Unit = with(bindingFragment) {
        textViewCountry.setOnClickListener {
            val action = SettingsFragmentDirections.actionNavigationSettingsToNavigationCountry()
            findNavController().navigate(action)
        }
        textViewQuran.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsToQuran()
            findNavController().navigate(action)
        }
        textViewHadeeths.setOnClickListener {
            val action =
                SettingsFragmentDirections.actionNavigationSettingsToNavigationHadeethsCategory()
            findNavController().navigate(action)
        }
    }

    override fun observeData() {

    }

    companion object {
        private const val TAG = "SettingsFragment"
    }
}