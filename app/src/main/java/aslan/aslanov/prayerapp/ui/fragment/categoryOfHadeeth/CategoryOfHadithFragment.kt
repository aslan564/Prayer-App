package aslan.aslanov.prayerapp.ui.fragment.categoryOfHadeeth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCategoryOfHadithBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranHadeethCategoryBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageHadeth
import aslan.aslanov.prayerapp.ui.fragment.categoryOfHadeeth.adapter.HadithCategoryAdapter
import aslan.aslanov.prayerapp.util.logApp
import aslan.aslanov.prayerapp.util.makeToast

@SuppressLint("ResourceType")
class CategoryOfHadithFragment : Fragment() {

    private val bindingFragment by lazy { FragmentCategoryOfHadithBinding.inflate(layoutInflater) }
    private lateinit var adapterHadith: HadithCategoryAdapter
    private val viewModel by viewModels<HadithCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    override fun onStart() {
        super.onStart()
        observeData()
    }
    private fun bindUI(): Unit = with(bindingFragment) {
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(
            onBackPressedCallback
        )
        swipeLayoutHadithCategory.setOnRefreshListener {
            viewModel.getHadithCategory()
            swipeLayoutHadithCategory.isRefreshing = false
        }
    }

    private fun observeData(): Unit = with(viewModel) {
        if (languageHadeth == null) {
            requireContext().makeToast(getString(R.string.language_quran))
        }
        categoryItem.observe(viewLifecycleOwner, { categoryItem ->
            categoryItem?.let {
                adapterHadith =
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
                bindingFragment.recyclerViewHadeethsCategory.apply {
                    layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    adapter = adapterHadith
                }
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