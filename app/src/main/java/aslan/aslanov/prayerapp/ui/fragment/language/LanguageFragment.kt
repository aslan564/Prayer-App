package aslan.aslanov.prayerapp.ui.fragment.language

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentLanguageBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranLanguageBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranLanguageHadithBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageHadeeth
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.languageSurah
import aslan.aslanov.prayerapp.ui.fragment.language.adapter.LanguageAdapter
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.makeToast


@SuppressLint("ResourceType")
class LanguageFragment : BaseFragment(R.layout.fragment_language) {

    private lateinit var binding: FragmentLanguageBinding
    private val viewModel by viewModels<LanguageViewModel>()

    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentLanguageBinding) {
            this.binding = binding
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
                            binding.recyclerViewLanguage.adapter = adapterLanguage
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
                                        languageHadeeth = data.code
                                        viewModel.clearHadeethsFromDb()
                                        viewModel.getHadithCategory {
                                            findNavController().popBackStack()
                                        }
                                    }
                                }
                            }
                            binding.recyclerViewLanguage.adapter = adapterLanguage
                        }
                    }
                    else -> {
                        null
                    }
                }
            }
        }
    }

    override fun observeData(): Unit = with(viewModel) {

        loading.observe(viewLifecycleOwner, { state ->
            if (state) {
                binding.progressBarQuranLanguage.visibility = View.VISIBLE
            } else {
                binding.progressBarQuranLanguage.visibility = View.GONE
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