package aslan.aslanov.prayerapp.ui.fragment.categoryOfHadeeth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCategoryOfHadithBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranHadeethCategoryBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageHadeeth
import aslan.aslanov.prayerapp.ui.fragment.categoryOfHadeeth.adapter.HadithCategoryAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.logApp
import aslan.aslanov.prayerapp.util.makeToast

@SuppressLint("ResourceType")
class CategoryOfHadithFragment : BaseFragment() {

    private val bindingFragment by lazy { FragmentCategoryOfHadithBinding.inflate(layoutInflater) }
    private lateinit var adapter: HadithCategoryAdapter
    private val viewModel by viewModels<HadithCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }

    override fun bindUI(): Unit = with(bindingFragment) {
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(
            this@CategoryOfHadithFragment,
            onBackPressedCallback
        )
        swipeLayoutHadithCategory.setOnRefreshListener {
            viewModel.getHadithCategory()
            swipeLayoutHadithCategory.isRefreshing = false
        }
    }

    override fun observeData(): Unit = with(viewModel) {
        if (languageHadeeth == null) {
            requireContext().makeToast(getString(R.string.language_quran))
        }
        categoryItem.observe(viewLifecycleOwner, { categoryItem ->
            categoryItem?.let {
                adapter =
                    HadithCategoryAdapter(categoryItem) { viewDataBinding, categoryItem, _, _ ->
                        if (viewDataBinding is LayoutItemQuranHadeethCategoryBinding) {
                            viewDataBinding.categoryItem = categoryItem
                            viewDataBinding.executePendingBindings()
                            viewDataBinding.root.setOnClickListener {
                                val action =
                                    CategoryOfHadithFragmentDirections.actionNavigationHadeethsCategoryToNavigationHadeeths(
                                        categoryItem
                                    )
                                findNavController().navigate(action)
                            }
                        }
                    }
                bindingFragment.recyclerViewHadeethsCategory.adapter = adapter
            } ?: requireContext().makeToast("please swipe layout")
        })
        error.observe(viewLifecycleOwner, {
            it?.let {
                requireContext().makeToast(it)
            }
        })
        loading.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    bindingFragment.progressBarHadithCategory.visibility = View.VISIBLE
                } else {
                    bindingFragment.progressBarHadithCategory.visibility = View.GONE
                }
            } ?: logApp(it.toString())
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quran_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_quran_language -> {
                val action =
                    CategoryOfHadithFragmentDirections.actionNavigationHadeethsCategoryToNavigationQuranLanguage()
                        .setLayoutId(R.layout.layout_item_quran_language_hadith)
                findNavController().navigate(action)
            }
        }
        return true
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }
}