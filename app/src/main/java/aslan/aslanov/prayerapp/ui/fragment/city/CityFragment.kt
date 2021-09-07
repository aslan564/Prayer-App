package aslan.aslanov.prayerapp.ui.fragment.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.databinding.FragmentCityBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemCityBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.AdapterCity


class CityFragment : Fragment() {
    private val cityViewModel by viewModels<CityViewModel>()
    private val binding by lazy { FragmentCityBinding.inflate(layoutInflater) }

    private val adapterCity by lazy {
        AdapterCity(onClickListener = { viewDataBinding, city ->
            if (viewDataBinding is LayoutItemCityBinding) {
                viewDataBinding.cityItem=city
                viewDataBinding.root.setOnClickListener {
                    SharedPreferenceManager.isLocation=false
                    SharedPreferenceManager.locationCountryName=city.countryCreatorId
                    SharedPreferenceManager.locationCityName=city.city
                    findNavController().popBackStack()
                }
            }
        })
    }

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
    }

    override fun onStart() {
        super.onStart()
        observeData()
    }

    private fun observeData(): Unit = with(cityViewModel) {

    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@CityFragment
        arguments?.let {
            CityFragmentArgs.fromBundle(it).countryName?.let { country ->
                cityViewModel.getAllCity(country){liveData->
                    liveData.observe(viewLifecycleOwner,{list->
                        list?.let {
                            adapterCity.submitList(list)
                        }
                    })
                }
            }
        }
        recyclerViewCity.adapter = adapterCity

    }

    companion object {
        private const val TAG = "CityFragment"
    }
}