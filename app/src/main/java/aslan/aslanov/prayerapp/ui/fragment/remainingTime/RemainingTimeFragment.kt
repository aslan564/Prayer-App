package aslan.aslanov.prayerapp.ui.fragment.remainingTime

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.FragmentRemainingTimeBinding
import aslan.aslanov.prayerapp.mainService.AlarmReceiver
import aslan.aslanov.prayerapp.mainService.EXTRA_MESSAGE
import aslan.aslanov.prayerapp.mainService.EXTRA_TYPE
import aslan.aslanov.prayerapp.model.ayahs.AyahEntity
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryEntity
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsConverted
import aslan.aslanov.prayerapp.model.whereWereWe.AyahsOrSurah
import aslan.aslanov.prayerapp.ui.fragment.settings.SettingsFragmentDirections
import aslan.aslanov.prayerapp.util.*
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType")
class RemainingTimeFragment : BaseFragment() {

    private val bindingFragment by lazy { FragmentRemainingTimeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RemainingViewModel>()
    private var currentDate = Calendar.getInstance()
    private lateinit var randomAyahRemaining:AyahEntity
    private lateinit var randomHadeethsRemaining:HadeethsEntity

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

    @SuppressLint("SimpleDateFormat")
    override fun bindUI(): Unit = with(bindingFragment) {
        imageViewShareHadeeths.setOnClickListener {
            share(randomHadeethsRemaining.categoryName, randomHadeethsRemaining.title, randomHadeethsRemaining.id,
                { intent: Intent ->
                    startActivity(Intent.createChooser(intent, null))
                }, { b: Boolean ->
                    requireActivity().runOnUiThread {
                        progressBar.isVisible=b
                    }
                })
        }
        imageViewShareAyah.setOnClickListener {
            share(randomAyahRemaining.surahEnglishName, randomAyahRemaining.text, randomAyahRemaining.number.toString(),
                { intent: Intent ->
                    startActivity(Intent.createChooser(intent, null))
                }, { b: Boolean ->
                    requireActivity().runOnUiThread {
                        progressBar.isVisible=b
                    }
                })
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
                    randomAyahRemaining = ayahs[random.nextInt(ayahs.size)]
                    bindingFragment.textViewDailyAyah.text = addRandomAyahsWithSurah(
                        randomAyahRemaining.number.toString(),
                        randomAyahRemaining.surahEnglishName,
                        randomAyahRemaining.surahArabicName,
                        randomAyahRemaining.text
                    )
                    bindingFragment.textViewDailyAyah.setOnClickListener {
                        val action =
                            RemainingTimeFragmentDirections.actionNavigationRemainingTimeToNavigationQuranAyahs(
                                randomAyahRemaining.surahEnglishName
                            )
                        action.surahNum = randomAyahRemaining.surahId
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
                     randomHadeethsRemaining = hadeeths[random.nextInt(hadeeths.size)]
                    bindingFragment.textViewDailyHadeeths.text = addRandomAyahsWithSurah(
                        randomHadeethsRemaining.id,
                        randomHadeethsRemaining.categoryId.toString(),
                        randomHadeethsRemaining.categoryName,
                        randomHadeethsRemaining.title
                    )
                    bindingFragment.textViewDailyHadeeths.setOnClickListener {
                        val category = CategoryEntity(
                            hadeeths.size.toString(),
                            randomHadeethsRemaining.categoryId.toString(),
                            "",
                            randomHadeethsRemaining.categoryName
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