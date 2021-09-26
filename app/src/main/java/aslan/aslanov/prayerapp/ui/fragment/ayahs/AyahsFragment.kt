package aslan.aslanov.prayerapp.ui.fragment.ayahs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentAyahsBinding
import aslan.aslanov.prayerapp.databinding.FragmentCurrentTimeBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranAyahsBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.model.whereWereWe.AyahsOrSurah
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.ui.activity.MainActivity
import aslan.aslanov.prayerapp.ui.fragment.ayahs.adapter.AyahsAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.logApp
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

        arguments?.let {
            val argsSurahNum = AyahsFragmentArgs.fromBundle(it).surahNum
            viewModel.getWhereWee(argsSurahNum)
            if (languageSurah != null) {
                viewModel.fetchSurahAyahs(argsSurahNum, languageSurah!!)
                getAndRefreshAyahs(argsSurahNum)
            }
            swipeLayoutAyahs.setOnRefreshListener {
                getAndRefreshAyahs(argsSurahNum)
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
                                whereWereWe = WhereWereWe(
                                    argsSurahNum,
                                    i,
                                    ayah.surahId,
                                    AyahsOrSurah.HADEETHS.name,
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

    companion object {

    }
}