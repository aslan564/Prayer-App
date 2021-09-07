package aslan.aslanov.prayerapp.ui.fragment.timings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.util.calculateTime
import aslan.aslanov.prayerapp.util.logApp
import java.util.*


class CurrentTimeFragment : Fragment(), View.OnClickListener {

    private val binding by lazy { FragmentCurrentTimeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<CurrentTimingsViewModel>()
    private lateinit var alarmReceiver: AlarmReceiver
    private var timings: TimingsEntity? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        observeData()
    }


    override fun onClick(view: View?): Unit = with(binding) {
        view?.let {
            if (timings != null) {
                when (view.id) {
                    binding.ivNotificationAsr.id -> {
                        checkAlarmState(isAsr, timings!!.asr!!, ivNotificationAsr) {
                            isAsr = it
                        }

                    }
                    binding.ivNotificationFajr.id -> {
                        checkAlarmState(isFajr, timings!!.fajr!!, ivNotificationFajr) {
                            isFajr = it
                        }
                    }
                    binding.ivNotificationMaghrib.id -> {
                        checkAlarmState(isMaghrib, timings!!.maghrib!!, ivNotificationMaghrib) {
                            isMaghrib = it
                        }
                    }
                    binding.ivNotificationDhuhr.id -> {
                        checkAlarmState(isDhuhur, timings!!.dhuhr!!, ivNotificationDhuhr) {
                            isDhuhur = it
                        }
                    }
                    binding.ivNotificationIsha.id -> {
                        checkAlarmState(isIsha, timings!!.isha!!, ivNotificationIsha) {
                            isIsha = it
                        }
                    }
                    binding.ivNotificationImsak.id -> {
                        checkAlarmState(isImsak, timings!!.imsak!!, ivNotificationImsak) {
                            isImsak = it
                        }
                    }
                    binding.ivNotificationMidnight.id -> {
                        checkAlarmState(isMidnight, timings!!.midnight!!, ivNotificationMidnight) {
                            isMidnight = it
                        }
                    }
                    binding.ivNotificationSunset.id -> {
                        checkAlarmState(isSunset, timings!!.sunset!!, ivNotificationSunset) {
                            isSunset = it
                        }
                    }
                    binding.ivNotificationSunrise.id -> {
                        checkAlarmState(isSunrise, timings!!.sunrise!!, ivNotificationSunrise) {
                            isSunrise = it
                        }
                    }
                }
            }
        }
    }


    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@CurrentTimeFragment
        alarmReceiver = AlarmReceiver()
        ivNotificationFajr.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationMaghrib.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationDhuhr.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationAsr.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationImsak.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationMidnight.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationSunset.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationSunrise.setOnClickListener(this@CurrentTimeFragment)
        ivNotificationIsha.setOnClickListener(this@CurrentTimeFragment)
        checkSharedPref()

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
                    1
                )
            }
            swipeLayoutCurrentTime.isRefreshing = false
        }

    }


    private fun setBgButtonThenSetAlarm(
        prayer: String,
        imageView: ImageView,
        onComplete: (String) -> Unit
    ) {
        timings?.let {
            imageView.setImageResource(R.drawable.ic_custom_notification_on)
            setAlarmManager(prayer)
            onComplete("Alarm Set Complete")
        }
    }

    private fun setButtonOfBgThenCancelAlarm(
        prayer: String,
        imageView: ImageView,
        onComplete: (String) -> Unit
    ) {
        timings?.let {
            imageView.setImageResource(R.drawable.ic_custom_notification_off)
            Toast.makeText(requireContext(), "alarm cancelled", Toast.LENGTH_SHORT).show()
            cancelAlarmManager(prayer)
            onComplete("Alarm Cancel Complete")
        }
    }

    private fun setAlarmManager(prayer: String) {
        val alarmTime=calculateTime(prayer)
        alarmReceiver.setAlarm(
            requireContext(), alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE), prayer
        )

    }

    private fun cancelAlarmManager(prayer: String) {
        alarmReceiver.cancelAlarm(
            requireContext(), calculateTime(prayer).get(Calendar.HOUR_OF_DAY), calculateTime(
                prayer
            ).get(Calendar.MINUTE), prayer
        )

    }

    private fun checkAlarmState(
        state: Boolean,
        times: String,
        imageView: ImageView,
        onComplete: (Boolean) -> Unit
    ) {
        if (!state) {
            setBgButtonThenSetAlarm(times, imageView) {
                onComplete(true)
            }
        } else {
            setButtonOfBgThenCancelAlarm(times, imageView) {
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

    private fun observeData(): Unit = with(viewModel) {
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