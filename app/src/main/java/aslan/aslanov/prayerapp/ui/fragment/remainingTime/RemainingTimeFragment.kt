package aslan.aslanov.prayerapp.ui.fragment.remainingTime

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
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
import aslan.aslanov.prayerapp.util.PendingRequests.REQUEST_CODE_AYAHS
import aslan.aslanov.prayerapp.util.PendingRequests.REQUEST_CODE_HADEETHS
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType")
class RemainingTimeFragment : BaseFragment() {

    private val bindingFragment by lazy { FragmentRemainingTimeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RemainingViewModel>()
    private var currentDate = Calendar.getInstance()
    private lateinit var randomAyahRemaining: AyahEntity
    private lateinit var randomHadeethsRemaining: HadeethsEntity
    private lateinit var category: CategoryEntity
    private lateinit var code: String

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

    override fun onStart() {
        super.onStart()
        code =
            requireActivity().intent.extras?.get(PendingRequests.CATCH_REQUEST_CODE_FROM_MAIN)
                .toString()
        getSpecificFragment()

    }

    @SuppressLint("SimpleDateFormat")
    override fun bindUI(): Unit = with(bindingFragment) {
        textViewDailyHadeeths.setOnClickListener {
            navigateFragment(REQUEST_CODE_HADEETHS)
        }
        textViewDailyAyah.setOnClickListener {
            navigateFragment(REQUEST_CODE_AYAHS)
        }
        textViewDailyHadeeths.setOnLongClickListener(View.OnLongClickListener {
            share(randomHadeethsRemaining.categoryName,
                randomHadeethsRemaining.title,
                randomHadeethsRemaining.id,
                { intent: Intent ->
                    startActivity(Intent.createChooser(intent, null))
                },
                { b: Boolean ->
                    requireActivity().runOnUiThread {
                        //progressBar.isVisible=b
                    }
                })
            return@OnLongClickListener true
        })

        textViewDailyAyah.setOnLongClickListener(View.OnLongClickListener {
            share(randomAyahRemaining.surahEnglishName,
                randomAyahRemaining.text,
                randomAyahRemaining.number.toString(),
                { intent: Intent ->
                    startActivity(Intent.createChooser(intent, null))
                },
                { b: Boolean ->
                    requireActivity().runOnUiThread {
                        //progressBar.isVisible=b
                    }
                })
            return@OnLongClickListener true
        })
        textViewNextPrayer.setOnClickListener {
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra(EXTRA_TYPE, AyahsOrSurah.AYAHS.name)
            intent.putExtra(EXTRA_MESSAGE, randomAyahRemaining.text)
            AlarmReceiver.showAlarmNotification(
                requireContext(),
                intent,
                REQUEST_CODE_AYAHS
            )
        }
        textViewPrayerTime.setOnClickListener {
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra(EXTRA_TYPE, AyahsOrSurah.HADEETHS.name)
            intent.putExtra(EXTRA_MESSAGE, randomHadeethsRemaining.title)
            AlarmReceiver.showAlarmNotification(
                requireContext(),
                intent,
                REQUEST_CODE_HADEETHS
            )
        }
    }

    override fun observeData(): Unit = with(viewModel) {
        currentTime.observe(viewLifecycleOwner, { time ->
            time?.let {
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
                    category = CategoryEntity(
                        hadeeths.size.toString(),
                        randomHadeethsRemaining.categoryId.toString(),
                        "",
                        randomHadeethsRemaining.categoryName
                    )
                } else {
                    bindingFragment.textViewDailyAyah.text = getString(R.string.hadeeths_text)
                }
            }
        })


    }

    private fun getSpecificFragment() {
        if (this::code.isInitialized) {
            when (code) {
                REQUEST_CODE_AYAHS.toString() -> {
                    navigateFragment(REQUEST_CODE_AYAHS)
                }
                REQUEST_CODE_HADEETHS.toString() -> {
                    navigateFragment(REQUEST_CODE_HADEETHS)
                }
                else -> {
                    Log.d(TAG, "onStart: $code")
                }
            }
        }
    }

    private fun navigateFragment(actionCode: Int) {
        if (actionCode == REQUEST_CODE_AYAHS) {
            if (this::randomAyahRemaining.isInitialized) {
                val action =
                    RemainingTimeFragmentDirections.actionNavigationRemainingTimeToNavigationQuranAyahs(
                        randomAyahRemaining.surahEnglishName
                    )
                action.surahNum = randomAyahRemaining.surahId
                findNavController().navigate(action)
            }
        } else {
            if (this::category.isInitialized) {
                val action =
                    RemainingTimeFragmentDirections.actionNavigationRemainingTimeToNavigationHadeeths(
                        category
                    )
                findNavController().navigate(action)
            }
        }


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