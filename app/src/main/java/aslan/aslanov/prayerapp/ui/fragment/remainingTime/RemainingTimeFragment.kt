package aslan.aslanov.prayerapp.ui.fragment.remainingTime

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentRemainingTimeBinding
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryEntity
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@SuppressLint("ResourceType")
class RemainingTimeFragment : BaseFragment(R.layout.fragment_remaining_time) {

    private lateinit var binding: FragmentRemainingTimeBinding
    private val viewModel by viewModels<RemainingViewModel>()
    private lateinit var currentDate: Calendar
    private var timeList = HashMap<Prayers,Date>()


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

    @SuppressLint("SimpleDateFormat")
    override fun bindUI(binding: ViewDataBinding) {
        super.bindUI(binding)
        if (binding is FragmentRemainingTimeBinding) {
            this.binding = binding
            currentDate = Calendar.getInstance()
        }
    }

    override fun observeData(): Unit = with(viewModel) {
        currentTime.observe(viewLifecycleOwner, { time ->
            time?.let {
                binding.progressBar.visibility = View.GONE
                createSortedList(it) { list ->
                    checkRemainingTimeHoursStatus(list)
                }
            }
        })

        randomAyah.observe(viewLifecycleOwner, { ayahs ->
            ayahs?.let {
                if (ayahs.isNotEmpty()) {
                    val random = Random()
                    val data = ayahs[random.nextInt(ayahs.size)]
                    binding.textViewDailyAyah.text = addRandomAyahsWithSurah(
                        data.number.toString(),
                        data.surahEnglishName,
                        data.surahArabicName,
                        data.text
                    )
                    binding.textViewDailyAyah.setOnClickListener {
                        val action =
                            RemainingTimeFragmentDirections.actionNavigationRemainingTimeToNavigationQuranAyahs()
                                .setSurahNum(data.surahId)
                        it.findNavController().navigate(action)
                    }

                } else {
                    binding.textViewDailyAyah.text = getString(R.string.ayah_text)
                }
            }
        })

        randomHadeeths.observe(viewLifecycleOwner, { hadeeths ->
            hadeeths?.let {
                if (hadeeths.isNotEmpty()) {
                    val random = Random()
                    val data = hadeeths[random.nextInt(hadeeths.size)]
                    binding.textViewDailyHadeeths.text = addRandomAyahsWithSurah(
                        data.id.toString(),
                        data.categoryId.toString(),
                        data.categoryName,
                        data.title
                    )
                    binding.textViewDailyHadeeths.setOnClickListener {
                        val category = CategoryEntity(
                            hadeeths.size.toString(),
                            data.categoryId.toString(),
                            "",
                            data.categoryName
                        )
                        val action =
                            RemainingTimeFragmentDirections.actionNavigationRemainingTimeToNavigationHadeeths(
                                category
                            )
                        it.findNavController().navigate(action)
                    }

                } else {
                    binding.textViewDailyAyah.text = getString(R.string.hadeeths_text)
                }
            }
        })
    }


    private fun addRandomAyahsWithSurah(
        umber: String,
        englishName: String,
        arabicName: String,
        text: String
    ): String {
        return "$umber / $englishName / ${arabicName}\n\n${text}"
    }


    private fun checkRemainingTimeHoursStatus(hashMap : HashMap<Prayers,Date>) {
        hashMap.entries.forEachIndexed{index, mutableEntry ->
            if (index < hashMap.size) {
                if (currentDate.time.after(mutableEntry.value) && currentDate.time.before(mutableEntry.value)) {
                    textViewStatus("$index")
                    //setCountDownTextView(list[index])
                }else{
                    logApp("$index")
                }
            }
        }
       /* { index, calendar ->
            if (index < list.size) {
                if (currentDate.time.after(calendar) && currentDate.time.before(list[index + 1])) {
                    textViewStatus("$index")
                    setCountDownTextView(list[index])
                }else{
                    logApp("$index")
                }
            }

        }*/


        /*  if (currentDate.time > list.first().time && currentDate.time <= list[1].time) {
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
  */
    }

    @SuppressLint("SetTextI18n")
    private fun setCountDownTextView(stopDate: Date): Unit = with(binding) {
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
        onComplete: (HashMap<Prayers,Date>) -> Unit
    ) {
        timeList[Prayers.ASR]=(calculateNextTime(it.asr!!).time)
        timeList[Prayers.IMSAK]=(calculateNextTime(it.imsak!!).time)
        timeList[Prayers.MAGHRIB]=(calculateNextTime(it.maghrib!!).time)
        timeList[Prayers.SUNRISE]=(calculateNextTime(it.sunrise!!).time)
        timeList[Prayers.MIDNIGHT]=(calculateNextTime(it.midnight!!).time)
        timeList[Prayers.FAJR]=(calculateNextTime(it.fajr!!).time)
        timeList[Prayers.DHUHUR]=(calculateNextTime(it.dhuhr!!).time)
        timeList[Prayers.ISHA]=(calculateNextTime(it.isha!!).time)
        timeList[Prayers.SUNSET]=(calculateNextTime(it.sunset!!).time)
        Log.d(TAG, "observeRemaining: ${timeList.size}")
        timeList.toSortedMap()
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