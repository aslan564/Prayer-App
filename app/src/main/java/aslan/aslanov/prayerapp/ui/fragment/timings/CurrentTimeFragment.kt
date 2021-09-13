package aslan.aslanov.prayerapp.ui.fragment.timings

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentCurrentTimeBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isAsr
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isDhuhur
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isFajr
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isImsak
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isIsha
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isMaghrib
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isMidnight
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isSunrise
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isSunset
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.locationCityName
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.locationCountryName
import aslan.aslanov.prayerapp.mainService.AlarmReceiver
import aslan.aslanov.prayerapp.mainService.EXTRA_MESSAGE
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.BaseFragment
import aslan.aslanov.prayerapp.util.calculateTime
import java.util.*


@SuppressLint("ResourceType")
class CurrentTimeFragment : BaseFragment(R.layout.fragment_current_time), View.OnClickListener {

    private lateinit var binding: FragmentCurrentTimeBinding
    private val viewModel by viewModels<CurrentTimingsViewModel>()
    private lateinit var alarmReceiver: AlarmReceiver
    private var timings: TimingsEntity? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        observeData()
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

    override fun onClick(view: View?): Unit = with(binding) {
        view?.let {
            if (timings != null) {
                when (view.id) {
                    binding.ivNotificationAsr.id -> {
                        checkAlarmState(
                            isAsr,
                            timings!!.asr!!,
                            ivNotificationAsr,
                            pendingIntentCreator()
                        ) {
                            isAsr = it
                        }

                    }
                    binding.ivNotificationFajr.id -> {
                        checkAlarmState(
                            isFajr,
                            timings!!.fajr!!,
                            ivNotificationFajr,
                            pendingIntentCreator()
                        ) {
                            isFajr = it
                        }
                    }
                    binding.ivNotificationMaghrib.id -> {
                        checkAlarmState(
                            isMaghrib,
                            timings!!.maghrib!!,
                            ivNotificationMaghrib,
                            pendingIntentCreator()
                        ) {
                            isMaghrib = it
                        }
                    }
                    binding.ivNotificationDhuhr.id -> {
                        checkAlarmState(
                            isDhuhur,
                            timings!!.dhuhr!!,
                            ivNotificationDhuhr,
                            pendingIntentCreator()
                        ) {
                            isDhuhur = it
                        }
                    }
                    binding.ivNotificationIsha.id -> {
                        checkAlarmState(
                            isIsha,
                            timings!!.isha!!,
                            ivNotificationIsha,
                            pendingIntentCreator()
                        ) {
                            isIsha = it
                        }
                    }
                    binding.ivNotificationImsak.id -> {
                        checkAlarmState(
                            isImsak,
                            timings!!.imsak!!,
                            ivNotificationImsak,
                            pendingIntentCreator()
                        ) {
                            isImsak = it
                        }
                    }
                    binding.ivNotificationMidnight.id -> {
                        checkAlarmState(
                            isMidnight,
                            timings!!.midnight!!,
                            ivNotificationMidnight,
                            pendingIntentCreator()
                        ) {
                            isMidnight = it
                        }
                    }
                    binding.ivNotificationSunset.id -> {
                        checkAlarmState(
                            isSunset,
                            timings!!.sunset!!,
                            ivNotificationSunset,
                            pendingIntentCreator()
                        ) {
                            isSunset = it
                        }
                    }
                    binding.ivNotificationSunrise.id -> {
                        checkAlarmState(
                            isSunrise,
                            timings!!.sunrise!!,
                            ivNotificationSunrise,
                            pendingIntentCreator()
                        ) {
                            isSunrise = it
                        }
                    }
                }
            }
        }
    }

    private fun pendingIntentCreator(): PendingIntent {
        val pendingRequestCode = Random().nextInt()
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, "prayer")
        Log.d(TAG, "pendingIntentCreator: $pendingRequestCode")
        return PendingIntent.getBroadcast(
            context,
            pendingRequestCode,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
    }

    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentCurrentTimeBinding) {
            this.binding = binding
            alarmReceiver = AlarmReceiver()
            binding.ivNotificationFajr.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationMaghrib.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationDhuhr.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationAsr.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationImsak.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationMidnight.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationSunset.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationSunrise.setOnClickListener(this@CurrentTimeFragment)
            binding.ivNotificationIsha.setOnClickListener(this@CurrentTimeFragment)
            checkSharedPref()

            binding.swipeLayoutCurrentTime.setOnRefreshListener {
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
                        1
                    )
                }
                binding.swipeLayoutCurrentTime.isRefreshing = false
            }
        }
    }

    private fun setBgButtonThenSetAlarm(
        prayer: String,
        imageView: ImageView,
        pendingIntent: PendingIntent,
        onComplete: (String) -> Unit
    ) {
        timings?.let {
            imageView.setImageResource(R.drawable.ic_custom_notification_on)
            setAlarmManager(prayer, pendingIntent)
            onComplete("Alarm Set Complete")
        }
    }

    private fun setButtonOfBgThenCancelAlarm(
        imageView: ImageView,
        pendingIntent: PendingIntent,
        onComplete: (String) -> Unit
    ) {
        timings?.let {
            imageView.setImageResource(R.drawable.ic_custom_notification_off)
            cancelAlarmManager(pendingIntent)
            onComplete("Alarm Cancel Complete")
        }
    }

    private fun setAlarmManager(prayer: String, pendingIntent: PendingIntent) {
        val alarmTime = calculateTime(prayer)
        Log.d(TAG, "setAlarmManager: ${alarmTime.time}")
        alarmReceiver.setAlarm(
            requireContext(),
            alarmTime.get(Calendar.HOUR_OF_DAY),
            alarmTime.get(Calendar.MINUTE),
            pendingIntent
        )

    }

    private fun cancelAlarmManager(pendingIntent: PendingIntent) {
        alarmReceiver.cancelAlarm(pendingIntent)
        Toast.makeText(requireContext(), "alarm cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun checkAlarmState(
        state: Boolean,
        times: String,
        imageView: ImageView,
        pendingIntent: PendingIntent,
        onComplete: (Boolean) -> Unit
    ) {
        if (!state) {
            setBgButtonThenSetAlarm(times, imageView, pendingIntent) {
                onComplete(true)
            }
        } else {
            setButtonOfBgThenCancelAlarm(imageView, pendingIntent) {
                onComplete(false)
            }
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

    override fun observeData(): Unit = with(viewModel) {
        if (locationCityName != null && locationCountryName != null) {
            binding.textViewCityName.text = locationCityName
            binding.textViewCountryName.text = locationCountryName
        }


        prayerTimingsLive.observe(viewLifecycleOwner, {
            it?.let {
                binding.currentPrayerTime = it
                timings = it
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
                    binding.progressCurrentTime.visibility = View.VISIBLE
                } else {
                    binding.progressCurrentTime.visibility = View.GONE
                }
            }
        })
    }

    private fun checkSharedPref(): Unit = with(binding) {
        if (isAsr) {
            buttonBgState(ivNotificationAsr, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationAsr, R.drawable.ic_custom_notification_off)
        }

        if (isDhuhur) {
            buttonBgState(ivNotificationDhuhr, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationDhuhr, R.drawable.ic_custom_notification_off)
        }

        if (isIsha) {
            buttonBgState(ivNotificationIsha, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationIsha, R.drawable.ic_custom_notification_off)
        }

        if (isMaghrib) {
            buttonBgState(ivNotificationMaghrib, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationMaghrib, R.drawable.ic_custom_notification_off)
        }

        if (isFajr) {
            buttonBgState(ivNotificationFajr, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationFajr, R.drawable.ic_custom_notification_off)
        }

        if (isSunrise) {
            buttonBgState(ivNotificationSunrise, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationSunrise, R.drawable.ic_custom_notification_off)
        }

        if (isSunset) {
            buttonBgState(ivNotificationSunset, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationSunset, R.drawable.ic_custom_notification_off)
        }

        if (isMidnight) {
            buttonBgState(ivNotificationMidnight, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationMidnight, R.drawable.ic_custom_notification_off)
        }

        if (isImsak) {
            buttonBgState(ivNotificationImsak, R.drawable.ic_custom_notification_on)
        } else {
            buttonBgState(ivNotificationImsak, R.drawable.ic_custom_notification_off)
        }
    }

    private fun buttonBgState(imageView: ImageView, drawable: Int) {
        imageView.setImageResource(drawable)
    }

    companion object {
        private const val TAG = "TimingsFragment"
    }


}