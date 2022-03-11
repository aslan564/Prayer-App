package aslan.aslanov.prayerapp.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsConverted
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import aslan.aslanov.prayerapp.network.NetworkResult
import com.google.gson.Gson
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "prayerapp.util"
val currentDate: Calendar = Calendar.getInstance()
fun logApp(msg: String) {
    Log.d(TAG, "logApp: $msg")
}

fun <T> catchServerError(error: ResponseBody?, onCatchError: (NetworkResult<T>) -> Unit) {
    try {
        if (error != null) {
            val jsonError = error.string()
            val gson = Gson()
            val text = gson.toJson(jsonError)
            onCatchError(NetworkResult.error(text))
        }
    } catch (e: Exception) {
        onCatchError(NetworkResult.error(e.message))
    }

}

@SuppressLint("SimpleDateFormat")
fun calculateTime(
    startDate: String,
): Calendar {
    val currentTime = Calendar.getInstance()
    val convertedCurrentTime = Calendar.getInstance()
    val alarmTime = Calendar.getInstance()
    try {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = formatter.parse(startDate)
        if (date != null) {
            alarmTime.time = date
            convertedCurrentTime.set(Calendar.HOUR_OF_DAY, alarmTime.get(Calendar.HOUR_OF_DAY))
            convertedCurrentTime.set(Calendar.MINUTE, alarmTime.get(Calendar.MINUTE))
            if (currentTime.time.after(convertedCurrentTime.time)) {
                convertedCurrentTime.add(Calendar.DATE, 1)
                logApp("currentTime ----------------${convertedCurrentTime.time}")
            } else {
                logApp("convertedCurrentTime ******${convertedCurrentTime.time}")
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return convertedCurrentTime
}

@SuppressLint("SimpleDateFormat")
fun calculateNextTime(
    startDate: String,
): Calendar {
    val calendar = Calendar.getInstance()
    val convertedCurrentTime = Calendar.getInstance()
    try {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = formatter.parse(startDate)
        if (date != null) {
            calendar.time = date
            convertedCurrentTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
            convertedCurrentTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return convertedCurrentTime
}


@SuppressLint("SimpleDateFormat")
fun timeDifference(
    stopDate: Date,
    onComplete: (elapsedHours: Long, elapsedMinutes: Long, elapsedSeconds: Long) -> Unit
) {
    try {
        SimpleDateFormat("HH:mm")
        val dateStart = currentDate.time
        val dateStop = stopDate.time
        val different = dateStop - dateStart.time

        val secondsInMilli = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli

        val elapsedHours = different / hoursInMilli

        val elapsedMinutes = different / minutesInMilli

        val elapsedSeconds = different / secondsInMilli

        onComplete(elapsedHours, elapsedMinutes, elapsedSeconds)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun countDownTimer(
    remainingHour: Int,
    remainingMinute: Int,
    remainingSecond: Int,
    remainingCounter: Long,
    onComplete: (hours: Int, minute: Int, second: Int, complete: Boolean) -> Unit
) {
    val countDownTimer: CountDownTimer
    var hours: Int = remainingHour
    var minute: Int = remainingMinute
    var second: Int = remainingSecond
    countDownTimer = object : CountDownTimer(remainingCounter, 1000) {
        override fun onTick(p0: Long) {
            if (second > 0) {
                second -= 1
            }
            if (minute > 0 && second == 0) {
                minute -= 1
                second = 60
            }
            if (hours > 0 && minute == 0) {
                hours -= 1
                minute = 60
            }
            onComplete(hours, minute, second, false)
        }

        override fun onFinish() {
            Log.d(TAG, "onFinish: complete")
            onComplete(0, 0, 0, true)
        }
    }
    countDownTimer.start()
}

fun SwipeRefreshLayout.swipe(onComplete: (Boolean) -> Unit) {
    this.setOnRefreshListener {
        onComplete(true)
    }
}


fun Context.makeToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun makeDialog(context: Context, isComplete: (Boolean) -> Unit) {
    val dialog = AlertDialog.Builder(context)
    dialog.setTitle(context.getString(R.string.share_content))
    dialog.setPositiveButton(context.getString(R.string.share)) { dialogInterface, _ ->
        isComplete.invoke(true)
        dialogInterface.dismiss()
    }
    dialog.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ ->
        isComplete.invoke(false)
        dialogInterface.dismiss()
    }
    dialog.setCancelable(false)
    dialog.show()
}

fun TimingsEntity.createSortedList(
    onComplete: (ArrayList<TimingsConverted>) -> Unit
) {
    val timeList = ArrayList<TimingsConverted>()
    timeList.add(TimingsConverted(1, Prayers.MIDNIGHT, calculateNextTime(this.midnight!!).time))
    timeList.add(TimingsConverted(2, Prayers.IMSAK, calculateNextTime(this.imsak!!).time))
    timeList.add(TimingsConverted(3, Prayers.FAJR, calculateNextTime(this.fajr!!).time))
    timeList.add(TimingsConverted(4, Prayers.SUNRISE, calculateNextTime(this.sunrise!!).time))
    timeList.add(TimingsConverted(5, Prayers.DHUHUR, calculateNextTime(this.dhuhr!!).time))
    timeList.add(TimingsConverted(6, Prayers.ASR, calculateNextTime(this.asr!!).time))
    timeList.add(TimingsConverted(7, Prayers.SUNSET, calculateNextTime(this.sunset!!).time))
    timeList.add(TimingsConverted(8, Prayers.MAGHRIB, calculateNextTime(this.maghrib!!).time))
    timeList.add(TimingsConverted(9, Prayers.ISHA, calculateNextTime(this.isha!!).time))
    timeList.sortedBy { timingsConverted ->
        timingsConverted.prayerTime
    }
    onComplete(timeList)
}

fun addRandomAyahsWithSurah(
    number: String,
    englishName: String,
    arabicName: String,
    text: String
): String {
    return "$number / $englishName / ${arabicName}\n\n${text}"
}

fun share(
    title: String,
    desc: String,
    surahNum: String,
    callback: (Intent) -> Unit,
    visibleCallback: (Boolean) -> Unit
) {
    visibleCallback.invoke(true)
    Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "$title $surahNum\n \n$desc")
        type = "text/*"
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        visibleCallback.invoke(false)
        callback.invoke(this)
    }

}

fun WhereWereWe?.isNullOrEmptyField(): WhereWereWe {
    return this ?: WhereWereWe("0", 0, 0, "0", "0")
}

fun getLanguage() = Locale.getDefault().language
