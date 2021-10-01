package aslan.aslanov.prayerapp.ui.fragment.timings

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCurrentTimeBinding
import aslan.aslanov.prayerapp.databinding.LayoutItemCurrentTimeBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isAsr
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isDhuhur
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isFajr
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isImsak
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isIsha
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isMaghrib
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isMidnight
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isSunrise
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.locationCityName
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.locationCountryName
import aslan.aslanov.prayerapp.mainService.AlarmReceiver.Companion.setAlarmManager
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsConverted
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.ui.activity.reading.ReadingActivity
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.ui.fragment.timings.adapter.CurrentTimeAdapter
import aslan.aslanov.prayerapp.util.*
import com.google.android.gms.tasks.OnCompleteListener


@SuppressLint("ResourceType")
class CurrentTimeFragment : Fragment() {

    private val bindingFragment by lazy { FragmentCurrentTimeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CurrentTimingsViewModel>()
    private lateinit var adapterCurrent: CurrentTimeAdapter
    private var timings: TimingsEntity? = null

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
        bindUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                requireActivity().startActivity(
                    Intent(requireContext(),
                        ReadingActivity::class.java)
                )
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        observeData()
    }


    private fun bindUI(): Unit = with(bindingFragment) {
        // checkSharedPref()

        swipeLayoutCurrentTime.setOnRefreshListener {
            if (locationCityName != null && locationCountryName != null) {
                getTimingsViewModel(
                    locationCityName!!,
                    locationCountryName!!,
                    8
                )
            } else {
                getTimingsViewModel(
                    "Baku",
                    "Azerbaijan",
                    8
                )
            }
             swipeLayoutCurrentTime.isRefreshing = false
        }
    }

    private fun observeData(): Unit = with(viewModel) {
        if (locationCityName != null && locationCountryName != null) {
            bindingFragment.textViewCityName.text = locationCityName
            bindingFragment.textViewCountryName.text = locationCountryName
        }

        prayerTimingsLive.observe(viewLifecycleOwner, {
            it?.let {
                it.createSortedList { list ->
                    adapterCurrent =
                        CurrentTimeAdapter(list) { viewDataBinding, timingsConverted, _, _ ->
                            if (viewDataBinding is LayoutItemCurrentTimeBinding) {
                                viewDataBinding.currentPrayerTime = timingsConverted
                                viewDataBinding.executePendingBindings()
                                logApp(timingsConverted.toString())
                                viewDataBinding.ivNotification.setOnClickListener {
                                    checkAlarmState(
                                        timingsConverted,
                                        viewDataBinding.ivNotification
                                    )
                                }
                            }
                        }
                    bindingFragment.recyclerViewCurrentTime.adapter = adapterCurrent
                }
            }
        })

        errorMessage.observe(viewLifecycleOwner, {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

        loading.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    bindingFragment.progressCurrentTime.visibility = View.VISIBLE
                } else {
                    bindingFragment.progressCurrentTime.visibility = View.GONE
                }
            }
        })
    }


    private fun setBgButtonThenSetAlarm(
        prayer: TimingsConverted,
        imageView: ImageView
    ) {
        imageView.setImageResource(R.drawable.ic_custom_notification_on)
        setAlarmManager(prayer, requireContext())
    }

    private fun setButtonOfBgThenCancelAlarm(
        prayer: TimingsConverted,
        imageView: ImageView
    ) {
        timings?.let {
            imageView.setImageResource(R.drawable.ic_custom_notification_off)
            //cancelAlarmManager(pendingIntent)
        }
    }

    private fun cancelAlarmManager(pendingIntent: PendingIntent) {
        val alarmReceiver = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //alarmReceiver.cancelAlarm(pendingIntent)
        Toast.makeText(requireContext(), "alarm cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun checkAlarmState(
        timingsConverted: TimingsConverted,
        ivNotification: ImageView
    ) {
        when (timingsConverted.prayerName) {
            Prayers.ASR -> {
                checkAlarmButtonState(isAsr, timingsConverted, ivNotification) {
                    isAsr = it
                }
            }
            Prayers.IMSAK -> {
                checkAlarmButtonState(isImsak, timingsConverted, ivNotification) {
                    isImsak = it
                }
            }
            Prayers.MAGHRIB -> {
                checkAlarmButtonState(isMaghrib, timingsConverted, ivNotification) {
                    isMaghrib = it
                }
            }
            Prayers.SUNRISE -> {
                checkAlarmButtonState(isSunrise, timingsConverted, ivNotification) {
                    isSunrise = it
                }
            }
            Prayers.MIDNIGHT -> {
                checkAlarmButtonState(isMidnight, timingsConverted, ivNotification) {
                    isMidnight = it
                }
            }
            Prayers.FAJR -> {
                checkAlarmButtonState(isFajr, timingsConverted, ivNotification) {
                    isFajr = it
                }
            }
            Prayers.DHUHUR -> {
                checkAlarmButtonState(isDhuhur, timingsConverted, ivNotification) {
                    isDhuhur = it
                }
            }
            Prayers.ISHA -> {
                checkAlarmButtonState(isIsha, timingsConverted, ivNotification) {
                    isIsha = it
                }
            }
            Prayers.SUNSET -> {
                checkAlarmButtonState(isSunrise, timingsConverted, ivNotification) {
                    isSunrise = it
                }
            }
        }
    }

    private fun checkAlarmButtonState(
        state: Boolean,
        timingsConverted: TimingsConverted,
        imageView: ImageView,
        onCompleteListener: (Boolean) -> Unit
    ) {
        if (!state) {
            setBgButtonThenSetAlarm(timingsConverted, imageView)
            onCompleteListener(true)
        } else {
            setButtonOfBgThenCancelAlarm(timingsConverted, imageView)
            onCompleteListener(false)
        }
    }

    private fun getTimingsViewModel(
        times: String, country: String, method: Int
    ): Unit = with(viewModel) {
        getTimingsPrayer(
            times,
            country,
            method
        )
    }


    private fun checkSharedPref(): Unit = with(bindingFragment) {
        if (isAsr) {
            // buttonBgState(ivNotificationAsr, R.drawable.ic_custom_notification_on)
        } else {
            // buttonBgState(ivNotificationAsr, R.drawable.ic_custom_notification_off)
        }
    }

    private fun buttonBgState(imageView: ImageView, drawable: Int) {
        imageView.setImageResource(drawable)
    }

    companion object {
        private const val TAG = "TimingsFragment"
    }


}