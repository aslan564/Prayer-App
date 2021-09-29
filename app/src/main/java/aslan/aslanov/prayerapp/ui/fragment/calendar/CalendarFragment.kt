package aslan.aslanov.prayerapp.ui.fragment.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCalendarBinding
import aslan.aslanov.prayerapp.databinding.LayoutCalendarItemBinding
import aslan.aslanov.prayerapp.ui.activity.ViewModelFactory
import aslan.aslanov.prayerapp.ui.fragment.calendar.adapter.CalendarAdapter
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.BaseFragment


@SuppressLint("ResourceType")
class CalendarFragment : BaseFragment() {
    private val bindingFragment by lazy { FragmentCalendarBinding.inflate(layoutInflater) }
    private val factory by lazy { ViewModelFactory(requireContext()) }
    private val viewModel by  lazy { ViewModelProvider(requireActivity(),factory).get(CalendarViewModel::class.java) }
    private val adapterCalendarFragment by lazy {
        CalendarAdapter { viewDataBinding, data ->
            if (viewDataBinding is LayoutCalendarItemBinding) {
                viewDataBinding.itemPrayer = data
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bindingFragment.root
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

    override fun bindUI():Unit= with(bindingFragment) {



            recyclerViewCalendar.apply {
                adapter=adapterCalendarFragment
            }
            viewModel.getMonthTimingByCity()
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
                    bindingFragment.progressBar.visibility = View.VISIBLE
                } else {
                    bindingFragment.progressBar.visibility = View.GONE
                }
            }
        })
    }

    companion object {
        private const val TAG = "CalendarFragment"
    }
}