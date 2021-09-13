package aslan.aslanov.prayerapp.ui.fragment.surah

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentQuranSurahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranSurahsBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.ui.fragment.surah.adapter.QuranSurahAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.makeToast

@SuppressLint("ResourceType")
class QuranSurahFragment : BaseFragment(R.layout.fragment_quran_surahs) {
    private var binding: FragmentQuranSurahsBinding? = null
    private val viewModel by viewModels<QuranSurahsViewModel>()
    private lateinit var quranSurahAdapter: QuranSurahAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quran_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_quran_language -> {
                val action =
                    QuranSurahFragmentDirections.actionNavigationQuranToNavigationQuranLanguage().setLayoutId(R.layout.layout_item_quran_language)
                findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")

    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentQuranSurahsBinding) {
            this.binding = binding
            binding.swipeLayoutSurah.setOnRefreshListener {
                getSurah()
                binding.swipeLayoutSurah.isRefreshing = false
            }
        }
    }

    override fun observeData(): Unit = with(viewModel) {

        getSurah()

        quranUiState.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding?.progressBarQuran?.visibility = View.VISIBLE
                } else {
                    binding?.progressBarQuran?.visibility = View.GONE
                }
            }
        })

        errorMessage.observe(viewLifecycleOwner, {
            it?.let {
                requireContext().makeToast(it)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getSurah() {
        viewModel.getSurahFromDB { quranResponse ->
            quranResponse.observe(viewLifecycleOwner, {
                it?.let {
                    quranSurahAdapter = QuranSurahAdapter(it) { viewDataBinding, list, i, surah ->
                        if (viewDataBinding is LayoutItemQuranSurahsBinding) {
                            viewDataBinding.quranItem = surah
                            viewDataBinding.root.setOnClickListener { v ->
                                if (languageSurah != null) {
                                    val action =
                                        QuranSurahFragmentDirections.actionNavigationQuranToNavigationQuranAyahs()
                                    action.surahNum = surah.number
                                    v.findNavController().navigate(action)
                                } else {
                                    requireContext().makeToast(requireContext().getString(R.string.language_quran))
                                }

                            }
                        }

                    }.apply { notifyDataSetChanged() }
                    binding?.recyclerViewQuran?.adapter = quranSurahAdapter
                }
            })
        }
    }


    companion object {

    }
}