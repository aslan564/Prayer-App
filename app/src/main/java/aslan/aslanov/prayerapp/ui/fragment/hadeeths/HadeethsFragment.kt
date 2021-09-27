package aslan.aslanov.prayerapp.ui.fragment.hadeeths

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentHadeethsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranHadeethBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageHadeeth
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.model.whereWereWe.AyahsOrSurah
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.ui.activity.MainActivity
import aslan.aslanov.prayerapp.ui.fragment.hadeeths.adapter.HadeethsAdapter
import aslan.aslanov.prayerapp.util.*
import com.google.android.material.snackbar.Snackbar
import kotlin.reflect.cast


@SuppressLint("ResourceType")
class HadeethsFragment : BaseFragment() {

    private val viewModel by viewModels<HadeethsViewModel>()
    private val bindingFragment by lazy { FragmentHadeethsBinding.inflate(layoutInflater) }
    private lateinit var adapterHadeeths: HadeethsAdapter
    private var page: Int = 1
    private var whereWereWe: WhereWereWe? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }


    override fun bindUI(): Unit = with(bindingFragment) {
        requireActivity().onBackPressedDispatcher.addCallback(
            this@HadeethsFragment,
            backPressedCallback
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (whereWereWe != null) {
            viewModel.setWhereWee(whereWereWe!!)
        }
    }


    override fun observeData(): Unit = with(viewModel) {
        arguments?.let {
            val category = HadeethsFragmentArgs.fromBundle(it).category
            viewModel.getWhereWee(category.title)

            (activity as MainActivity).supportActionBar!!.title = category.title
            if (languageHadeeth != null) {
                addHadith(
                    category.id.toInt(),
                    page,
                    50,
                    category.title
                )
            } else {
                findNavController().popBackStack()
            }
            getHadithFromDb(category.id.toInt()).observe(viewLifecycleOwner, { res ->
                if (res != null && res.isNotEmpty()) {
                    adapterHadeeths =
                        HadeethsAdapter(res) { viewDataBinding, data, _, positionAdapter ->
                            if (viewDataBinding is LayoutItemQuranHadeethBinding) {
                                viewDataBinding.hadithItem = data

                                whereWereWe = WhereWereWe(
                                    data.categoryName,
                                    positionAdapter,
                                    data.id.toInt(),
                                    AyahsOrSurah.HADEETHS.name,
                                    data.categoryName
                                )
                                viewDataBinding.root.setOnClickListener { view ->
                                    view.makeDialog(requireContext()) { state ->
                                        if (state) {
                                            share(data.categoryName, data.title, data.id,
                                                { intent: Intent ->
                                                    startActivity(
                                                        Intent.createChooser(
                                                            intent,
                                                            null
                                                        )
                                                    )
                                                }, { b: Boolean ->
                                                    requireActivity().runOnUiThread {
                                                        bindingFragment.progressBarHadith.isVisible =
                                                            b
                                                    }
                                                })
                                        }
                                    }
                                }
                            }
                        }
                    bindingFragment.recyclerViewHadeeths.adapter = adapterHadeeths
                    bindingFragment.recyclerViewHadeeths.addOnScrollListener(
                        recyclerViewScrollChangeListener
                    )
                } else {
                    logApp("**************************************** ${res.size}")
                }
            })
            whereWereLiveData.observe(viewLifecycleOwner, { whereWereLiveData ->
                whereWereLiveData?.let {
                    iJustWantToScroll(whereWereLiveData.position)
                }
            })

            error.observe(viewLifecycleOwner, { error ->
                error?.let {
                    requireContext().makeToast(error)
                }
            })
            loading.observe(viewLifecycleOwner, { state ->
                state?.let {
                    if (state) {
                        bindingFragment.progressBarHadith.visibility = View.VISIBLE
                    } else {
                        bindingFragment.progressBarHadith.visibility = View.GONE
                    }
                }
            })

        }
    }

    private val recyclerViewScrollChangeListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager =
                LinearLayoutManager::class.cast(bindingFragment.recyclerViewHadeeths.layoutManager)
            val totalItemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastVisibleItemPosition()

            val endHasBeenReached = lastVisible + 5 >= totalItemCount
            if (totalItemCount > 0 && endHasBeenReached) {
                arguments?.let {
                    val category = HadeethsFragmentArgs.fromBundle(it).category
                    if (languageHadeeth != null) {
                        viewModel.addHadith(
                            category.id.toInt(),
                            page++,
                            50,
                            category.title
                        )
                    }
                }
            }
        }
    }

    private fun iJustWantToScroll(position: Int): Unit = with(bindingFragment) {
        recyclerViewHadeeths.postDelayed({
            recyclerViewHadeeths.scrollToPosition(position)
        }, 1000)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (whereWereWe != null) {
                viewModel.setWhereWee(whereWereWe!!)
            }
            findNavController().popBackStack()
        }
    }

    companion object {
        const val HADEETHS_POSITION = "hadeeth_position"
    }
}

