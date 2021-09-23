package aslan.aslanov.prayerapp.ui.fragment.hadeeths

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentHadeethsBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranHadeethBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageHadeeth
import aslan.aslanov.prayerapp.ui.activity.MainActivity
import aslan.aslanov.prayerapp.ui.fragment.hadeeths.adapter.HadeethsAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.logApp
import aslan.aslanov.prayerapp.util.makeToast
import kotlin.reflect.cast


@SuppressLint("ResourceType")
class HadeethsFragment : BaseFragment(R.layout.fragment_hadeeths) {

    private val viewModel by viewModels<HadeethsViewModel>()
    private lateinit var binding: FragmentHadeethsBinding
    private lateinit var adapterHadeeths: HadeethsAdapter
    private var page: Int = 1
    private var savedPage: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            savedPage = savedInstanceState.getInt(HADEETHS_POSITION, 0)
        }
    }

    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentHadeethsBinding) {
            this.binding = binding
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val position = binding.recyclerViewHadeeths.scrollState
        outState.putInt(HADEETHS_POSITION, position)
    }

    override fun observeData(): Unit = with(viewModel) {
        arguments?.let {
            val category = HadeethsFragmentArgs.fromBundle(it).category
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
                        HadeethsAdapter(res) { viewDataBinding, data, _, _ ->

                            if (viewDataBinding is LayoutItemQuranHadeethBinding) {
                                viewDataBinding.hadithItem = data
                            }
                        }
                    binding.recyclerViewHadeeths.adapter = adapterHadeeths
                    adapterHadeeths.registerAdapterDataObserver(object :
                        RecyclerView.AdapterDataObserver() {
                        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                            savedPage?.let { it1 ->
                                binding.recyclerViewHadeeths.scrollToPosition(
                                    it1
                                )
                            }
                        }
                    })
                    binding.recyclerViewHadeeths.addOnScrollListener(
                        recyclerViewScrollChangeListener
                    )
                    iJustWantToScroll()
                } else {
                    logApp("**************************************** ${res.size}")
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
                        binding.progressBarHadith.visibility = View.VISIBLE
                    } else {
                        binding.progressBarHadith.visibility = View.GONE
                    }
                }
            })

        }
    }

    private val recyclerViewScrollChangeListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager =
                LinearLayoutManager::class.cast(binding.recyclerViewHadeeths.layoutManager)
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
                    // requireContext().makeToast("$endHasBeenReached $totalItemCount")
                }
            }
        }
    }

    private fun iJustWantToScroll(): Unit = with(binding) {
        savedPage?.let {
            recyclerViewHadeeths.postDelayed({
                recyclerViewHadeeths.scrollToPosition(it)
            }, 1000)
        }
    }

    companion object {
        const val HADEETHS_POSITION = "hadeeth_position"
    }
}

