package aslan.aslanov.prayerapp.ui.fragment.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCalendarBinding
import aslan.aslanov.prayerapp.databinding.LayoutCalendarItemBinding
import aslan.aslanov.prayerapp.ui.fragment.calendar.adapter.CalendarAdapter
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.BaseFragment


@SuppressLint("ResourceType")
class CalendarFragment : BaseFragment(R.layout.fragment_calendar) {
    private lateinit var binding : FragmentCalendarBinding
    private val viewModel by viewModels<CalendarViewModel>()
    private val adapterCalendarFragment by lazy {
        CalendarAdapter { viewDataBinding, data ->
            if (viewDataBinding is LayoutCalendarItemBinding) {
                val itemBinding=viewDataBinding as LayoutCalendarItemBinding
                itemBinding.itemPrayer=data
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val action = SettingsFragmentDirections.actionToNavigateToSettings()
                findNavController().navigate(action)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentCalendarBinding) {
            this.binding = binding
            binding.recyclerViewCalendar.apply {
                adapter=adapterCalendarFragment
            }
            viewModel.getMonthTimingByCity()
        }
    }

    override fun observeData(): Unit = with(viewModel) {
        viewModel.prayerTimeByHijriCalendar.observe(viewLifecycleOwner, {
            it?.let {
                adapterCalendarFragment.submitList(it.data)
            }
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            it?.let {
                android.widget.Toast.makeText(requireContext(), it, android.widget.Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                } else {
                    binding.progressBar.visibility = android.view.View.GONE
                }
            }
        })
    }

    companion object {
        private const val TAG = "CalendarFragment"
    }
}