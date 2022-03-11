package aslan.aslanov.prayerapp.ui.fragment.surah

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentQuranSurahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranSurahsBinding
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.model.baseViewModel.ViewModelFactory
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.ui.fragment.surah.adapter.QuranSurahAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.makeToast

@SuppressLint("ResourceType")
class QuranSurahFragment : BaseFragment() {
    private val bindingFragment by lazy { FragmentQuranSurahsBinding.inflate(layoutInflater) }
    private val viewModel: QuranSurahsViewModel by activityViewModels {
        ViewModelFactory(
            PrayerDatabase.getInstance(requireContext()),
            requireContext(),
            RetrofitService
        )
    }


    private lateinit var quranSurahAdapter: QuranSurahAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.fetchSurahs()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quran_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
               R.id.menu_quran_language -> {
                   val action =
                       QuranSurahFragmentDirections.actionNavigationQuranToNavigationQuranLanguage()
                           .setLayoutId(R.layout.layout_item_quran_language)
                   findNavController().navigate(action)
               }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")

    override fun bindUI(): Unit = with(bindingFragment) {
        swipeLayoutSurah.setOnRefreshListener {
            viewModel.fetchSurahs()
            swipeLayoutSurah.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun observeData() {
        viewModel.apply {
            data.observe(viewLifecycleOwner) {
                it?.let {
                    Log.d("ReadingActivity", "observeData: $it")
                    quranSurahAdapter = QuranSurahAdapter(it) { viewDataBinding, _, _, surah ->
                        if (viewDataBinding is LayoutItemQuranSurahsBinding) {
                            viewDataBinding.quranItem = surah
                            viewDataBinding.root.setOnClickListener { v ->
                                languageSurah?.let { it1 ->
                                    getAyahFromInternet(surah.number, it1) { response ->
                                        addAyahDb(surah, response)
                                    }
                                }
                                val action =
                                    QuranSurahFragmentDirections.actionNavigationQuranToNavigationQuranAyahs(
                                        surah
                                    )
                                v.findNavController().navigate(action)
                            }
                        }

                    }.apply { notifyDataSetChanged() }
                    bindingFragment.recyclerViewQuran.adapter = quranSurahAdapter
                }
            }
            loadingState.observe(viewLifecycleOwner) {
                it?.let {
                    if (it) {
                        bindingFragment.progressBarQuran.visibility = View.VISIBLE
                    } else
                        bindingFragment.progressBarQuran.visibility = View.GONE
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                it?.let {
                    requireContext().makeToast(it)
                }
            }
        }
    }


    companion object {
        private const val TAG = "QuranSurahFragment"
    }
}