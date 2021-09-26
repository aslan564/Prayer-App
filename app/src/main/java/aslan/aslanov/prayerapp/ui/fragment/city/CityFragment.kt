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
class CityFragment : BaseFragment() {
    private val cityViewModel by viewModels<CityViewModel>()
    private val bindingFragment by lazy { FragmentCityBinding.inflate(layoutInflater) }

    private val adapterCity by lazy {
        AdapterCity(onClickListener = { viewDataBinding, city ->
            if (viewDataBinding is LayoutItemCityBinding) {
                viewDataBinding.cityItem = city
                viewDataBinding.root.setOnClickListener {
                    SharedPreferenceManager.isLocation = false
                    SharedPreferenceManager.locationCountryName = city.countryCreatorId
                    SharedPreferenceManager.locationCityName = city.city
                    findNavController().popBackStack()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
    }

    override fun observeData(): Unit = with(cityViewModel) {
        arguments?.let {
            CityFragmentArgs.fromBundle(it).countryName?.let { country ->
                cityViewModel.getAllCity(country) { liveData ->
                    liveData.observe(viewLifecycleOwner, { list ->
                        list?.let {
                            adapterCity.submitList(list)
                        }
                    })
                }
            }
        }
        bindingFragment.recyclerViewCity.adapter = adapterCity

    }

    override fun bindUI(): Unit = with(bindingFragment) {

    }


    companion object {
        private const val TAG = "CityFragment"
    }
}