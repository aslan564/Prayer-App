package aslan.aslanov.prayerapp.ui.fragment.country

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import aslan.aslanov.prayerapp.databinding.FragmentCountriesBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemCountryBinding
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.baseViewModel.ViewModelFactory
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.ui.fragment.country.adapterCountry.AdapterCountry
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.makeToast


class CountryFragment : BaseFragment() {
    private val bindingFragment by lazy { FragmentCountriesBinding.inflate(layoutInflater) }
    private val viewModel: CountryViewModel by viewModels {
        ViewModelFactory(
            PrayerDatabase.getInstance(
                requireContext()
            ), requireContext(), RetrofitService
        )
    }
    private var adapterCountry: AdapterCountry? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingFragment.root


    override fun bindUI(): Unit = with(bindingFragment) {
        viewModel.getCountry()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun observeData(): Unit = with(viewModel) {
        errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                requireContext().makeToast(it)
            }
        }

        data.observe(viewLifecycleOwner) {
            it?.let {
                adapterCountry =
                    AdapterCountry(
                        it,
                        onCompletionListener = { viewDataBinding: ViewDataBinding, list: List<CountryWithCities>, i: Int, item: CountryWithCities ->
                            if (viewDataBinding is LayoutItemCountryBinding) {
                                viewDataBinding.countryItem = list[i].country
                                viewDataBinding.root.setOnClickListener { view ->
                                    val action =
                                        CountryFragmentDirections.actionNavigationCountryToNavigationCity()
                                            .setCountryName(item.country.country)
                                    view.findNavController().navigate(action)
                                }
                            }
                        })
                bindingFragment.recyclerViewCountryTest.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = adapterCountry
                }
            }
        }


        loadingState.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    bindingFragment.progressBar.visibility = View.VISIBLE
                } else {
                    bindingFragment.progressBar.visibility = View.GONE
                }
            }
        }
    }


    companion object {
        //private const val TAG = "CountryFragment"
    }
}