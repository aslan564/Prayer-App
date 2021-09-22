package aslan.aslanov.prayerapp.ui.fragment.timings

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.ViewDataBinding
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
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.ui.fragment.timings.adapter.CurrentTimeAdapter
import aslan.aslanov.prayerapp.util.*
import com.google.android.gms.tasks.OnCompleteListener


@SuppressLint("ResourceType")
class CurrentTimeFragment : BaseFragment(R.layout.fragment_current_time), View.OnClickListener {

    private lateinit var binding: FragmentCurrentTimeBinding
    private val viewModel by viewModels<CurrentTimingsViewModel>()
    private lateinit var adapterCurrent: CurrentTimeAdapter
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
            /*  if (timings != null) {
                  when (view.id) {
                      binding.ivNotificationAsr.id -> {
                          checkAlarmState(
                              isAsr,
                              timings!!.asr!!,
                              ivNotificationAsr
                          ) {
                              isAsr = it
                          }
                      }
                      binding.ivNotificationFajr.id -> {
                          checkAlarmState(
                              isFajr,
                              timings!!.fajr!!,
                              ivNotificationFajr
                          ) {
                              isFajr = it
                          }
                      }
                      binding.ivNotificationMaghrib.id -> {
                          checkAlarmState(
                              isMaghrib,
                              timings!!.maghrib!!,
                              ivNotificationMaghrib
                          ) {
                              isMaghrib = it
                          }
                      }
                      binding.ivNotificationDhuhr.id -> {
                          checkAlarmState(
                              isDhuhur,
                              timings!!.dhuhr!!,
                              ivNotificationDhuhr
                          ) {
                              isDhuhur = it
                          }
                      }
                      binding.ivNotificationIsha.id -> {
                          checkAlarmState(
                              isIsha,
                              timings!!.isha!!,
                              ivNotificationIsha
                          ) {
                              isIsha = it
                          }
                      }
                      binding.ivNotificationImsak.id -> {
                          checkAlarmState(
                              isImsak,
                              timings!!.imsak!!,
                              ivNotificationImsak
                          ) {
                              isImsak = it
                          }
                      }
                      binding.ivNotificationMidnight.id -> {
                          checkAlarmState(
                              isMidnight,
                              timings!!.midnight!!,
                              ivNotificationMidnight
                          ) {
                              isMidnight = it
                          }
                      }
                      binding.ivNotificationSunset.id -> {
                          checkAlarmState(
                              isSunset,
                              timings!!.sunset!!,
                              ivNotificationSunset
                          ) {
                              isSunset = it
                          }
                      }
                      binding.ivNotificationSunrise.id -> {
                          checkAlarmState(
                              isSunrise,
                              timings!!.sunrise!!,
                              ivNotificationSunrise
                          ) {
                              isSunrise = it
                          }
                      }
                  }
              }*/
        }
    }

    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentCurrentTimeBinding) {
            this.binding = binding
            checkSharedPref()

            binding.swipeLayoutCurrentTime.swipe {
                if (it) {
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
    }
    override fun observeData(): Unit = with(viewModel) {
        if (locationCityName != null && locationCountryName != null) {
            binding.textViewCityName.text = locationCityName
            binding.textViewCountryName.text = locationCountryName
        }

        prayerTimingsLive.observe(viewLifecycleOwner, {
            it?.let {
                it.createSortedList { list ->
                    adapterCurrent =
                        CurrentTimeAdapter(list) { viewDataBinding, timingsConverted, _, i ->
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
                    binding.recyclerViewCurrentTime.adapter = adapterCurrent
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
                    binding.progressCurrentTime.visibility = View.VISIBLE
                } else {
                    binding.progressCurrentTime.visibility = View.GONE
                }
            }
        })
    }



    private fun setBgButtonThenSetAlarm(
        prayer: TimingsConverted,
        imageView: ImageView
    ) {
        imageView.setImageResource(R.drawable.ic_custom_notification_on)
        setAlarmManager(prayer,requireContext())
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
                checkAlarmButtonState(isAsr, timingsConverted, ivNotification){
                    isAsr=it
                }
            }
            Prayers.IMSAK -> {
                checkAlarmButtonState(isImsak, timingsConverted, ivNotification){
                    isImsak=it
                }
            }
            Prayers.MAGHRIB -> {
                checkAlarmButtonState(isMaghrib, timingsConverted, ivNotification){
                    isMaghrib=it
                }
            }
            Prayers.SUNRISE -> {
                checkAlarmButtonState(isSunrise, timingsConverted, ivNotification){
                    isSunrise=it
                }
            }
            Prayers.MIDNIGHT -> {
                checkAlarmButtonState(isMidnight, timingsConverted, ivNotification){
                    isMidnight=it
                }
            }
            Prayers.FAJR -> {
                checkAlarmButtonState(isFajr, timingsConverted, ivNotification){
                    isFajr=it
                }
            }
            Prayers.DHUHUR -> {
                checkAlarmButtonState(isDhuhur, timingsConverted, ivNotification){
                    isDhuhur=it
                }
            }
            Prayers.ISHA -> {
                checkAlarmButtonState(isIsha, timingsConverted, ivNotification){
                    isIsha=it
                }
            }
            Prayers.SUNSET -> {
                checkAlarmButtonState(isSunrise, timingsConverted, ivNotification){
                    isSunrise=it
                }
            }
        }
    }

    private fun checkAlarmButtonState(
        state: Boolean,
        timingsConverted: TimingsConverted,
        imageView: ImageView,
        onCompleteListener: (Boolean)->Unit
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


    private fun checkSharedPref(): Unit = with(binding) {
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