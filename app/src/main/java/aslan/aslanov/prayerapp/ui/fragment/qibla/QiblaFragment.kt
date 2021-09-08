package aslan.aslanov.prayerapp.ui.fragment.qibla

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentQiblaBinding
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections


class QiblaFragment : Fragment() {

    private val binding by lazy { FragmentQiblaBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@QiblaFragment

    }


    companion object {

    }
}