package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentAyahsBinding
import aslan.aslanov.prayerapp.util.makeToast

class AyahsFragment : Fragment() {

    private val binding by lazy { FragmentAyahsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<AyahsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeAyahs()
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@AyahsFragment
        arguments?.let {
            val args = AyahsFragmentArgs.fromBundle(it).surahNum
            requireContext().makeToast("$args")
        }

        recyclerViewAyahs.setOnClickListener {
            val action = AyahsFragmentDirections.actionNavigationQuranAyahsToNavigationQuranAyah().setAyahNum(1)
            requireView().findNavController().navigate(action)
        }
    }

    private fun observeAyahs(): Unit = with(viewModel) {


    }


    companion object {

    }
}