package aslan.aslanov.prayerapp.ui.fragment.surah

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentQuranSurahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranSurahsBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.quranLanguage
import aslan.aslanov.prayerapp.ui.fragment.surah.adapter.QuranSurahAdapter
import aslan.aslanov.prayerapp.util.makeToast

class QuranSurahFragment : Fragment() {

    private val binding by lazy { FragmentQuranSurahsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<QuranSurahsViewModel>()
    private lateinit var quranSurahAdapter: QuranSurahAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        bindUI()
        observeQuranData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quran_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_quran_language -> {
                val action =
                    QuranSurahFragmentDirections.actionNavigationQuranToNavigationQuranLanguage()
                findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@QuranSurahFragment
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeQuranData(): Unit = with(viewModel) {
        quranResponse.observe(viewLifecycleOwner, {
            it?.let {
                quranSurahAdapter = QuranSurahAdapter(it) { viewDataBinding, list, i, surah ->
                    if (viewDataBinding is LayoutItemQuranSurahsBinding) {
                        viewDataBinding.quranItem = surah
                        viewDataBinding.root.setOnClickListener { v ->
                            if (quranLanguage != null) {
                                val action =
                                    QuranSurahFragmentDirections.actionNavigationQuranToNavigationQuranAyahs()
                                action.surahNum = surah.number!!
                                v.findNavController().navigate(action)
                            } else {
                                requireContext().makeToast(requireContext().getString(R.string.language_quran))
                            }

                        }
                    }

                }.apply { notifyDataSetChanged() }
                binding.recyclerViewQuran.adapter = quranSurahAdapter
            }
        })

        quranUiState.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.progressBarQuran.visibility = View.VISIBLE
                } else {
                    binding.progressBarQuran.visibility = View.GONE
                }
            }
        })
        errorMessage.observe(viewLifecycleOwner,{
            it?.let {
                requireContext().makeToast(it)
            }
        })
    }


    companion object {

    }
}