package aslan.aslanov.prayerapp.ui.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import aslan.aslanov.prayerapp.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private val binding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    override fun onStart() {
        super.onStart()
        observeData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun bindUI(): Unit = with(binding) {
        textViewCountry.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsToCountry()
            it.findNavController().navigate(action)
        }
        textViewQuran.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsToQuran()
            it.findNavController().navigate(action)
        }
    }

    private fun observeData(): Unit {

    }

    companion object {
        private const val TAG = "SettingsFragment"
    }
}