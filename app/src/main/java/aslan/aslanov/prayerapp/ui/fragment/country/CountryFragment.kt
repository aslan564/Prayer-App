package aslan.aslanov.prayerapp.ui.fragment.country

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import aslan.aslanov.prayerapp.databinding.FragmentCountryBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemCountryBinding
import aslan.aslanov.prayerapp.model.countryModel.CountryWithCities
import aslan.aslanov.prayerapp.ui.fragment.country.adapterCountry.AdapterCountry


@SuppressLint("ResourceType")
class CountryFragment : Fragment() {
    private val binding by lazy { FragmentCountryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CountryViewModel>()
    private var adapterCountry: AdapterCountry? = null


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
        observeData()
    }

    override fun onStart() {
        super.onStart()


    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observeData(): Unit = with(viewModel) {
        getAllCountry()

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
                                        CountryFragmentDirections.actionCountryToCityFragment()
                                            .setCountryName(item.country.country)
                                    view.findNavController().navigate(action)
                                }
                            }
                        }).apply { notifyDataSetChanged() }
                binding.recyclerViewCountry.adapter = adapterCountry

            }
        })


        state.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@CountryFragment

    }

    companion object {
        private const val TAG = "CountryFragment"
    }
}