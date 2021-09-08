package aslan.aslanov.prayerapp.ui.fragment.language

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentLanguageBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemQuranLanguageBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.quranLanguage
import aslan.aslanov.prayerapp.ui.fragment.language.adapter.LanguageAdapter


class LanguageFragment : Fragment() {

    private val binding by lazy { FragmentLanguageBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LanguageViewModel>()
    private lateinit var adapterLanguage: LanguageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeLanguage()
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@LanguageFragment
    }

    private fun observeLanguage(): Unit = with(viewModel) {
        viewModel.getLanguage()
        languageResponse.observe(viewLifecycleOwner, { language ->
            language?.let {
                adapterLanguage= LanguageAdapter(it.data!!){ viewDataBinding, data, list, i ->
                    if (viewDataBinding is LayoutItemQuranLanguageBinding) {
                        viewDataBinding.languageItem=data
                        viewDataBinding.executePendingBindings()
                        viewDataBinding.root.setOnClickListener {
                            quranLanguage=data.identifier
                            findNavController().popBackStack()
                        }
                    }
                }
                binding.recyclerViewLanguage.adapter=adapterLanguage
            }
        })
        loading.observe(viewLifecycleOwner,{state->
            if (state) {
                binding.progressBarQuranLanguage.visibility=View.VISIBLE
            }else{
                binding.progressBarQuranLanguage.visibility=View.GONE
            }

        })

    }

    companion object {
    }
}