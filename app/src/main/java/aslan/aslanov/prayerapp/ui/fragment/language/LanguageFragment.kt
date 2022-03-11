package aslan.aslanov.prayerapp.ui.fragment.language

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentLanguageBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranLanguageBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranLanguageHadithBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageHadeth
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.ui.fragment.language.adapter.LanguageAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.makeToast


@SuppressLint("ResourceType")
class LanguageFragment : BaseFragment() {

    private val bindingFragment by lazy { FragmentLanguageBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LanguageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }

    override fun bindUI(): Unit = with(bindingFragment) {
        arguments?.let {
            when (LanguageFragmentArgs.fromBundle(it).layoutId) {
                R.layout.layout_item_quran_language -> {
                    viewModel.getLanguageSurah { language ->
                        val adapterLanguage = LanguageAdapter(
                            language.data!!,
                            R.layout.layout_item_quran_language
                        ) { viewDataBinding, data, _, _ ->
                            if (viewDataBinding is LayoutItemQuranLanguageBinding) {
                                viewDataBinding.languageItem = data
                                viewDataBinding.executePendingBindings()
                                viewDataBinding.root.setOnClickListener {
                                    languageSurah = data.identifier
                                    findNavController().popBackStack()
                                }
                            }
                        }
                        recyclerViewLanguage.adapter = adapterLanguage
                    }
                }
                R.layout.layout_item_quran_language_hadith -> {
                    viewModel.getLanguageHadeeth { language ->
                        val adapterLanguage = LanguageAdapter(
                            language,
                            R.layout.layout_item_quran_language_hadith
                        ) { viewDataBinding, data, _, _ ->
                            if (viewDataBinding is LayoutItemQuranLanguageHadithBinding) {
                                viewDataBinding.languageItem = data
                                viewDataBinding.executePendingBindings()
                                viewDataBinding.root.setOnClickListener {
                                    languageHadeth = data.code
                                    viewModel.clearHadeethsFromDb()
                                    viewModel.getHadithCategory {
                                        findNavController().popBackStack()
                                    }
                                }
                            }
                        }
                        recyclerViewLanguage.adapter = adapterLanguage
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun observeData(): Unit = with(viewModel) {

        loading.observe(viewLifecycleOwner, { state ->
            if (state) {
                bindingFragment.progressBarQuranLanguage.visibility = View.VISIBLE
            } else {
                bindingFragment.progressBarQuranLanguage.visibility = View.GONE
            }

        })
        errorMessage.observe(viewLifecycleOwner, { msg ->
            msg?.let {
                requireContext().makeToast(it)
            }
        })
    }


    companion object {
    }
}