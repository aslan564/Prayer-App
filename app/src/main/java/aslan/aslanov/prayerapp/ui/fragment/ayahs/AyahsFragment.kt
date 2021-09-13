package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.annotation.SuppressLint
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentAyahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranAyahsBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.ui.activity.MainActivity
import aslan.aslanov.prayerapp.ui.fragment.ayahs.adapter.AyahsAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.logApp
import aslan.aslanov.prayerapp.util.makeToast

@SuppressLint("ResourceType")
class AyahsFragment : BaseFragment(R.layout.fragment_ayahs) {

    private lateinit var binding: FragmentAyahsBinding
    private val viewModel by viewModels<AyahsViewModel>()
    private var ayahsAdapter: AyahsAdapter? = null


    @SuppressLint("NotifyDataSetChanged")
    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentAyahsBinding) {
            this.binding = binding
            binding.viewModelSurahName = viewModel

            arguments?.let {
                val argsSurahNum = AyahsFragmentArgs.fromBundle(it).surahNum
                if (languageSurah != null) {
                    viewModel.fetchSurahAyahs(argsSurahNum, languageSurah!!)
                    getAndRefreshAyahs(argsSurahNum)
                }
                binding.swipeLayoutAyahs.setOnRefreshListener {
                    getAndRefreshAyahs(argsSurahNum)
                    binding.swipeLayoutAyahs.isRefreshing = false
                }
            }

        }
    }

    override fun observeData(): Unit = with(viewModel) {
        ayahsStatus.observe(viewLifecycleOwner, { status ->
            if (status) {
                binding.progressBarQuranAyahs.visibility = View.VISIBLE
            } else {
                binding.progressBarQuranAyahs.visibility = View.GONE
            }
        })
        errorMsg.observe(viewLifecycleOwner, {
            it?.let {
                requireContext().makeToast(it)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAndRefreshAyahs(
        argsSurahNum: Int
    ): Unit = with(binding) {
        viewModel.getAyahsFromDatabase(argsSurahNum) { liveData ->
            liveData.observe(viewLifecycleOwner, { ayahs ->
                ayahs?.let {
                    logApp(ayahs.toString())
                    ayahsAdapter =
                        AyahsAdapter(ayahs) { viewDataBinding, ayah, list, i ->
                            textViewSurahName.text = ayah.surahEnglishName
                            (activity as MainActivity).supportActionBar!!.title =
                                ayah.surahArabicName
                            if (viewDataBinding is LayoutItemQuranAyahsBinding) {
                                viewDataBinding.quranItem = ayah
                                viewDataBinding.executePendingBindings()
                            }
                        }.apply { notifyDataSetChanged() }
                    binding.recyclerViewAyahs.adapter = ayahsAdapter
                }
            })
        }
    }


    companion object {

    }
}