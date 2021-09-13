package aslan.aslanov.prayerapp.ui.fragment.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentSettingsBinding
import aslan.aslanov.prayerapp.util.BaseFragment


@SuppressLint("ResourceType")
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

    }

    override fun onStart() {
        super.onStart()
        observeData()
    }


    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentSettingsBinding) {
            binding.textViewCountry.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsToCountry()
                it?.findNavController()?.navigate(action)
            }
            binding.textViewQuran.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsToQuran()
                it?.findNavController()?.navigate(action)
            }
            binding.textViewHadeeths.setOnClickListener {
                val action =
                    SettingsFragmentDirections.actionNavigationSettingsToNavigationHadeethsCategory()
                it?.findNavController()?.navigate(action)
            }

        }
    }

    override fun observeData(){

    }

    companion object {
        private const val TAG = "SettingsFragment"
    }
}