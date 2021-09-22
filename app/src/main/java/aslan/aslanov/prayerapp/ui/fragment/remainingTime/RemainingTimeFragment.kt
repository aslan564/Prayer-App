package aslan.aslanov.prayerapp.ui.fragment.remainingTime

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentRemainingTimeBinding
import aslan.aslanov.prayerapp.mainService.AlarmReceiver
import aslan.aslanov.prayerapp.mainService.AlarmReceiver.Companion.showAlarmNotification
import aslan.aslanov.prayerapp.mainService.EXTRA_MESSAGE
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryEntity
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsConverted
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType")
class RemainingTimeFragment : BaseFragment(R.layout.fragment_remaining_time) {

    private lateinit var bindingFragment: FragmentRemainingTimeBinding
    private val viewModel by viewModels<RemainingViewModel>()
    private var currentDate = Calendar.getInstance()


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
            this.bindingFragment = binding
            bindingFragment.textViewNextPrayer.setOnClickListener {

            }
        }
    }

    override fun observeData(): Unit = with(viewModel) {
        currentTime.observe(viewLifecycleOwner, { time ->
            time?.let {
                bindingFragment.progressBar.visibility = View.GONE
                it.createSortedList { list ->
                    checkRemainingTimeHoursStatus(list)
                }
            }
        })

        randomAyah.observe(viewLifecycleOwner, { ayahs ->
            ayahs?.let {
                if (ayahs.isNotEmpty()) {
                    val random = Random()
                    val data = ayahs[random.nextInt(ayahs.size)]
                    bindingFragment.textViewDailyAyah.text = addRandomAyahsWithSurah(
                        data.number.toString(),
                        data.surahEnglishName,
                        data.surahArabicName,
                        data.text
                    )
                    bindingFragment.textViewDailyAyah.setOnClickListener {
                        val action =
                            RemainingTimeFragmentDirections.actionNavigationRemainingTimeToNavigationQuranAyahs()
                                .setSurahNum(data.surahId)
                        it.findNavController().navigate(action)
                    }

                } else {
                    bindingFragment.textViewDailyAyah.text = getString(R.string.ayah_text)
                }
            }
        })

        randomHadeeths.observe(viewLifecycleOwner, { hadeeths ->
            hadeeths?.let {
                if (hadeeths.isNotEmpty()) {
                    val random = Random()
                    val data = hadeeths[random.nextInt(hadeeths.size)]
                    bindingFragment.textViewDailyHadeeths.text = addRandomAyahsWithSurah(
                        data.id,
                        data.categoryId.toString(),
                        data.categoryName,
                        data.title
                    )
                    bindingFragment.textViewDailyHadeeths.setOnClickListener {
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
                    bindingFragment.textViewDailyAyah.text = getString(R.string.hadeeths_text)
                }
            }
        })
    }





    private fun checkRemainingTimeHoursStatus(hashMap: ArrayList<TimingsConverted>) {
        hashMap.forEachIndexed { index, mutableEntry ->
            if (index < hashMap.size) {
                if (currentDate.time.after(mutableEntry.prayerTime) && currentDate.time.before(
                        hashMap[index + 1].prayerTime
                    )
                ) {
                    textViewStatus(hashMap[index + 1].prayerName.name.lowercase(Locale.getDefault()))
                    setCountDownTextView(hashMap[index + 1].prayerTime)
                } else {
                    logApp("$index")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCountDownTextView(stopDate: Date): Unit = with(bindingFragment) {
        timeDifference(
            stopDate
        ) { elapsedHours: Long, elapsedMinutes: Long, elapsedSeconds: Long ->
            contentLoadingProgress.min
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


    private fun textViewStatus(string: String): Unit = with(bindingFragment) {
        textViewNextPrayer.text = string
        logApp(string)
    }

    companion object {
        private const val TAG = "RemainingTimeFragment"
    }
}