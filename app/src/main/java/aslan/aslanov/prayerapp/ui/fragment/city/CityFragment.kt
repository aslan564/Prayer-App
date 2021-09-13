package aslan.aslanov.prayerapp.ui.fragment.city

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCityBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemCityBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.ui.fragment.city.adapterCity.AdapterCity
import aslan.aslanov.prayerapp.util.BaseFragment


@SuppressLint("ResourceType")
class CityFragment : BaseFragment(R.layout.fragment_city) {
    private val cityViewModel by viewModels<CityViewModel>()
    private lateinit var binding : FragmentCityBinding

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun observeData(): Unit = with(cityViewModel) {
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
        binding.recyclerViewCity.adapter = adapterCity

    }

    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentCityBinding) {
            this.binding = binding

        }
    }



    companion object {
        private const val TAG = "CityFragment"
    }
}