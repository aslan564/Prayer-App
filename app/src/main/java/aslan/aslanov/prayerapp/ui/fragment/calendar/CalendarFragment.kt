package aslan.aslanov.prayerapp.ui.fragment.calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCalendarBinding
import aslan.aslanov.prayerapp.databinding.LayoutCalendarItemBinding
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.baseViewModel.ViewModelFactory
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.ui.activity.reading.ReadingActivity
import aslan.aslanov.prayerapp.ui.fragment.calendar.adapter.CalendarAdapter
import aslan.aslanov.prayerapp.util.BaseFragment


@SuppressLint("ResourceType")
class CalendarFragment : BaseFragment() {
    private val bindingFragment by lazy { FragmentCalendarBinding.inflate(layoutInflater) }
    private val viewModel:CalendarViewModel by viewModels {
        ViewModelFactory(PrayerDatabase.getInstance(requireContext()),requireContext(), RetrofitService)

    }
    private lateinit var adapterCalendarFragment :
        CalendarAdapter

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
                requireActivity().startActivity(Intent(requireContext(),ReadingActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun bindUI(): Unit = with(bindingFragment) {

        viewModel.getMonthTimingByCity()
    }

    override fun observeData(): Unit = with(viewModel) {
        viewModel.prayerTimeByHijriCalendar.observe(viewLifecycleOwner) {
            it?.let { list ->
                adapterCalendarFragment =
                    CalendarAdapter(list.data!!) { viewDataBinding, data, _, _ ->
                        if (viewDataBinding is LayoutCalendarItemBinding) {
                            viewDataBinding.itemPrayer = data
                            viewDataBinding.executePendingBindings()
                        }
                    }
                bindingFragment.recyclerViewCalendar.apply {
                    adapter = adapterCalendarFragment
                }
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                android.widget.Toast.makeText(
                    requireContext(),
                    it,
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
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
        private const val TAG = "CalendarFragment"
    }
}