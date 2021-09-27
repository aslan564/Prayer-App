package aslan.aslanov.prayerapp.ui.fragment.country

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCountryBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemCountryBinding
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.ui.fragment.country.adapterCountry.AdapterCountry
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.addRandomAyahsWithSurah


class CountryFragment : Fragment() {
    private val bindingFragment by lazy { FragmentCountryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CountryViewModel>()
    private var adapterCountry: AdapterCountry? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingFragment.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    override fun onStart() {
        super.onStart()
        observeData()
    }

    private fun bindUI(): Unit = with(bindingFragment) {
        bindingFragment.swipeLayoutCountry.setOnRefreshListener {
            viewModel.getCountryDatabase()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData(): Unit = with(viewModel) {
        countryListError.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        country.observe(viewLifecycleOwner, {
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
                bindingFragment.recyclerViewCountry.adapter = adapterCountry
            }
        })


        state.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    bindingFragment.progressBar.visibility = View.VISIBLE
                } else {
                    bindingFragment.progressBar.visibility = View.GONE
                }
            }
        })
    }


    companion object {
        //private const val TAG = "CountryFragment"
    }
}