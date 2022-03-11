package aslan.aslanov.prayerapp.ui.activity.reading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.ActivityReadingBinding
import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager
import aslan.aslanov.prayerapp.model.baseViewModel.ViewModelFactory
import aslan.aslanov.prayerapp.network.RetrofitService
import aslan.aslanov.prayerapp.ui.activity.reading.viewModel.ReadingActivityViewModel
import aslan.aslanov.prayerapp.util.getLanguage

class ReadingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityReadingBinding.inflate(layoutInflater) }

    private val readingViewModel: ReadingActivityViewModel by viewModels {
        ViewModelFactory(
            PrayerDatabase.getInstance(this), this, RetrofitService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: ${getLanguage()}")
        readingViewModel.apply {
            getQuran()
            data.observe(this@ReadingActivity){
                it?.let {response->
                    Log.d(TAG, "onCreate: $it")
                    response.data?.let {surah->
                        addSurahDb(surah)
                    }
                }
            }
            errorMessage.observe(this@ReadingActivity){
                it?.let {
                    Log.d(TAG, "onCreate: $it")
                }
            }
            loadingState.observe(this@ReadingActivity){
                it?.let {
                    Log.d(TAG, "onCreate: $it")
                }
            }
        }
    }
    companion object{
        private const val TAG = "ReadingActivity"
    }
}