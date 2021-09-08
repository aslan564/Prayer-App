package aslan.aslanov.prayerapp.ui.fragment.remainingTime

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentRemainingTimeBinding
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.*
import java.util.*
import kotlin.collections.ArrayList


class RemainingTimeFragment : Fragment() {

    private val binding by lazy { FragmentRemainingTimeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RemainingViewModel>()
    private lateinit var currentDate: Calendar
    private var timeList = ArrayList<Calendar>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        bindUI()

    }


    override fun onStart() {
        super.onStart()
        observeRemaining()
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

    @SuppressLint("SimpleDateFormat")
    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@RemainingTimeFragment
        currentDate = Calendar.getInstance()

    }

    private fun observeRemaining(): Unit = with(viewModel) {
        currentTime.observe(viewLifecycleOwner, { time ->
            time?.let {
                binding.progressBar.visibility = View.GONE
                createSortedList(it) { list ->
                    checkRemainingTimeHoursStatus(list)
                }
            }
        })
    }

    private fun checkRemainingTimeHoursStatus(list: java.util.ArrayList<Calendar>) {
        if (currentDate.time > list.first().time && currentDate.time <= list[1].time) {
            textViewStatus(requireContext().getString(R.string.midnight))
            setCountDownTextView(list[1])
        }
        if (currentDate.time > list[1].time && currentDate.time <= list[2].time) {
            textViewStatus(requireContext().getString(R.string.imsak))
            setCountDownTextView(list[2])
        }
        if (currentDate.time > list[2].time && currentDate.time <= list[3].time) {
            textViewStatus(requireContext().getString(R.string.fajr))
            setCountDownTextView(list[3])
        }
        if (currentDate.time > list[3].time && currentDate.time <= list[4].time) {
            textViewStatus(requireContext().getString(R.string.sunrise))
            setCountDownTextView(list[4])
        }
        if (currentDate.time > list[4].time && currentDate.time <= list[5].time) {
            textViewStatus(requireContext().getString(R.string.dhuhr))
            setCountDownTextView(list[5])
        }
        if (currentDate.time > list[5].time && currentDate.time <= list[6].time) {
            textViewStatus(requireContext().getString(R.string.asr))
            setCountDownTextView(list[6])
        }
        if (currentDate.time > list[6].time && currentDate.time <= list[7].time) {
            textViewStatus(requireContext().getString(R.string.sunset))
            setCountDownTextView(list[7])
        }
        if (currentDate.time > list[7].time && currentDate.time <= list[8].time) {
            textViewStatus(requireContext().getString(R.string.maghrib))
            setCountDownTextView(list[8])
        }
        if (currentDate.time > list[8].time && currentDate.time <= calculateNextTime(list[0]).time) {
            textViewStatus(requireContext().getString(R.string.isha))
            setCountDownTextView(calculateNextTime(list[0]))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setCountDownTextView(stopDate: Calendar): Unit = with(binding) {
        timeDifference(
            currentDate,
            stopDate
        ) { elapsedHours: Long, elapsedMinutes: Long, elapsedSeconds: Long ->
            contentLoadingProgress.max = ((elapsedHours * 60) * 1000).toInt()
            val minuteDif = if (elapsedMinutes > 60) {
                elapsedMinutes % 60
            } else {
                elapsedMinutes
            }
            countDownTimer(
                elapsedHours.toInt(),
                minuteDif.toInt(),
                (elapsedSeconds / 1000).toInt(),
                (elapsedMinutes * 60) * 1000
            ) { hours, minute, second, complete ->
                if (complete) {
                    textViewPrayerTime.text = "Time Up"
                } else {
                    textViewPrayerTime.text = "$hours:$minute:$second"
                    contentLoadingProgress.progress = minute
                }
            }
        }
    }

    private fun createSortedList(
        it: TimingsEntity,
        onComplete: (java.util.ArrayList<Calendar>) -> Unit
    ) {
        timeList.add(calculateTime(it.asr!!))
        timeList.add(calculateTime(it.imsak!!))
        timeList.add(calculateTime(it.maghrib!!))
        timeList.add(calculateTime(it.sunrise!!))
        timeList.add(calculateTime(it.midnight!!))
        timeList.add(calculateTime(it.fajr!!))
        timeList.add(calculateTime(it.dhuhr!!))
        timeList.add(calculateTime(it.isha!!))
        timeList.add(calculateTime(it.sunset!!))
        Log.d(TAG, "observeRemaining: ${timeList.size}")
        timeList.sort()
        onComplete(timeList)
    }


    private fun textViewStatus(string: String): Unit = with(binding) {
        textViewNextPrayer.text = string
        logApp(string)
    }

    companion object {
        private const val TAG = "RemainingTimeFragment"
    }
}