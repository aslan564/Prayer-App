package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.databinding.FragmentAyahsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranAyahsBinding
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.model.baseViewModel.ViewModelFactory
import aslan.aslanov.prayerapp.model.newQuranModel.Surah
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.model.whereWereWe.AyahsOrSurah
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.ui.activity.main.MainActivity
import aslan.aslanov.prayerapp.ui.activity.reading.ReadingActivity
import aslan.aslanov.prayerapp.ui.fragment.ayahs.adapter.AyahsAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.isNullOrEmptyField
import aslan.aslanov.prayerapp.util.makeToast

@SuppressLint("ResourceType")
class AyahsFragment : BaseFragment() {

    private val bindingFragment by lazy { FragmentAyahsBinding.inflate(layoutInflater) }
    private val viewModel: AyahsViewModel by viewModels {
        ViewModelFactory(
            PrayerDatabase.getInstance(requireContext()),
            requireContext(),
            RetrofitService
        )
    }
    private var ayahsAdapter: AyahsAdapter? = null
    private var whereWereWe: WhereWereWe? = null
    private var surahEntity: SurahEntity? = null

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
        requireActivity().onBackPressedDispatcher.addCallback(
            this@AyahsFragment,
            onBackPressedCallback
        )
        arguments?.let {
            AyahsFragmentArgs.fromBundle(it).surahData?.let { surah ->
                viewModel.getWhereWee(surah.name)
                surahEntity = surah
                getAndRefreshAyahs(surah.number)
                bindingFragment.swipeLayoutAyahs.setOnRefreshListener {
                    getAndRefreshAyahs(surah.number)
                    bindingFragment.swipeLayoutAyahs.isRefreshing = false
                }
            }
        }
    }

    override fun observeData(): Unit = with(viewModel) {
        loadingState.observe(viewLifecycleOwner) { status ->
            if (status) {
                bindingFragment.progressBarQuranAyahs.visibility = View.VISIBLE
            } else {
                bindingFragment.progressBarQuranAyahs.visibility = View.GONE
            }
        }
        errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                requireContext().makeToast(it)
            }
        }

        data.observe(viewLifecycleOwner) { whereWereLiveData ->
            whereWereLiveData?.let {
                iJustWantToScroll(whereWereLiveData.position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAndRefreshAyahs(
        argsSurahNum: Int
    ) {
        viewModel.fetchSurahAyahs(argsSurahNum).observe(viewLifecycleOwner) { response ->
            Log.d(TAG, "observeData: $response")
            response?.let { ayahs ->
                bindingFragment.apply {
                    ayahsAdapter =
                        AyahsAdapter(ayahs) { viewDataBinding, ayah, _, i ->
                            textViewSurahName.text = ayah.surahEnglishName
                            (activity as ReadingActivity).supportActionBar!!.title =
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
            }
        }
    }

    private fun iJustWantToScroll(position: Int): Unit = with(bindingFragment) {
        recyclerViewAyahs.postDelayed({
            recyclerViewAyahs.scrollToPosition(position)
        }, 1000)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.setWhereWee(whereWereWe.isNullOrEmptyField())
            findNavController().popBackStack()
        }

    }

    companion object {
        private const val TAG = "AyahsFragment"
    }
}