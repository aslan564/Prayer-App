package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentAyahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranAyahsBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.quranLanguage
import aslan.aslanov.prayerapp.ui.fragment.ayahs.adapter.AyahsAdapter
import aslan.aslanov.prayerapp.util.logApp
import aslan.aslanov.prayerapp.util.makeToast

class AyahsFragment : Fragment() {

    private val binding by lazy { FragmentAyahsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<AyahsViewModel>()
    private var ayahsAdapter: AyahsAdapter? = null

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
        viewModelSurahName = viewModel

        arguments?.let {
            val argsSurahNum = AyahsFragmentArgs.fromBundle(it).surahNum
            if (quranLanguage != null) {
                viewModel.fetchSurahAyahs(argsSurahNum, quranLanguage!!)
            }

        }

        recyclerViewAyahs.setOnClickListener {
            val action = AyahsFragmentDirections.actionNavigationQuranAyahsToNavigationQuranAyah()
                .setAyahNum(1)
            requireView().findNavController().navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeAyahs(): Unit = with(viewModel) {
        ayahs.observe(viewLifecycleOwner, { ayahs ->
            ayahs?.let {
                logApp(ayahs.data!!.ayahs!!.toString())
                ayahsAdapter =
                    AyahsAdapter(ayahs.data!!.ayahs!!) { viewDataBinding, ayah, list, i ->
                        if (viewDataBinding is LayoutItemQuranAyahsBinding) {
                            viewDataBinding.quranItem = ayah
                            viewDataBinding.executePendingBindings()
                        }
                    }.apply { notifyDataSetChanged() }
                binding.recyclerViewAyahs.adapter = ayahsAdapter
            }
        })
        ayahsStatus.observe(viewLifecycleOwner, { status ->
            if (status) {
                binding.progressBarQuranAyahs.visibility = View.VISIBLE
            } else {
                binding.progressBarQuranAyahs.visibility = View.GONE
            }

        })

    }


    companion object {

    }
}