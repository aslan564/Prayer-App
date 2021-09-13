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
class QiblaFragment : BaseFragment(R.layout.fragment_qibla) {

    private lateinit var binding : FragmentQiblaBinding


    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentQiblaBinding) {
            this.binding = binding
        }
    }

    override fun observeData() {

    }


    companion object {

    }
}