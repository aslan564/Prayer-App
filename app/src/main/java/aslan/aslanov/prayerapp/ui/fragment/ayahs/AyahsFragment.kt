package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.databinding.FragmentAyahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranAyahsBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.model.whereWereWe.AyahsOrSurah
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.ui.activity.main.MainActivity
import aslan.aslanov.prayerapp.ui.fragment.ayahs.adapter.AyahsAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.makeToast

@SuppressLint("ResourceType")
class AyahsFragment : BaseFragment() {

    private val bindingFragment by lazy { FragmentAyahsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<AyahsViewModel>()
    private var ayahsAdapter: AyahsAdapter? = null
    private var whereWereWe: WhereWereWe? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (whereWereWe != null) {
            viewModel.setWhereWee(whereWereWe!!)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindUI(): Unit = with(bindingFragment) {
        viewModelSurahName = viewModel
        requireActivity().onBackPressedDispatcher.addCallback(this@AyahsFragment,onBackPressedCallback)

        arguments?.let {
            val surahName = AyahsFragmentArgs.fromBundle(it).surahName
            val surahNum = AyahsFragmentArgs.fromBundle(it).surahNum
            viewModel.getWhereWee(surahName)
            if (languageSurah != null) {
                getAndRefreshAyahs(surahNum)
            }
            swipeLayoutAyahs.setOnRefreshListener {
                getAndRefreshAyahs(surahNum)
                swipeLayoutAyahs.isRefreshing = false
            }
        }

    }

    override fun observeData(): Unit = with(viewModel) {
        ayahsStatus.observe(viewLifecycleOwner, { status ->
            if (status) {
                bindingFragment.progressBarQuranAyahs.visibility = View.VISIBLE
            } else {
                bindingFragment.progressBarQuranAyahs.visibility = View.GONE
            }
        })
        errorMsg.observe(viewLifecycleOwner, {
            it?.let {
                requireContext().makeToast(it)
            }
        })

        whereWereLiveData.observe(viewLifecycleOwner, { whereWereLiveData ->
            whereWereLiveData?.let {
                iJustWantToScroll(whereWereLiveData.position)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAndRefreshAyahs(
        argsSurahNum: Int
    ): Unit = with(bindingFragment) {
        viewModel.getAyahsFromDatabase(argsSurahNum) { liveData ->
            liveData.observe(viewLifecycleOwner, { ayahs ->
                if (ayahs!=null) {
                    if (ayahs.isEmpty()) {
                        viewModel.fetchSurahAyahs(argsSurahNum, languageSurah!!)
                        return@observe
                    }
                    ayahsAdapter =
                        AyahsAdapter(ayahs) { viewDataBinding, ayah, _, i ->
                            textViewSurahName.text = ayah.surahEnglishName
                            (activity as MainActivity).supportActionBar!!.title =
                                ayah.surahArabicName
                            if (viewDataBinding is LayoutItemQuranAyahsBinding) {
                                viewDataBinding.quranItem = ayah
                                viewDataBinding.executePendingBindings()
                                whereWereWe = WhereWereWe(
                                    ayah.surahEnglishName,
                                    i,
                                    ayah.surahId,
                                    AyahsOrSurah.AYAHS.name,
                                    ayah.surahEnglishName
                                )
                            }
                        }.apply { notifyDataSetChanged() }
                    recyclerViewAyahs.adapter = ayahsAdapter
                }

            })
        }
    }

    private fun iJustWantToScroll(position: Int): Unit = with(bindingFragment) {
        recyclerViewAyahs.postDelayed({
            recyclerViewAyahs.scrollToPosition(position)
        }, 1000)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (whereWereWe != null) {
                viewModel.setWhereWee(whereWereWe!!)
            }
            findNavController().popBackStack()
        }

    }

    companion object {

    }
}