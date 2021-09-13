package aslan.aslanov.prayerapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import aslan.aslanov.prayerapp.model.countryModel.*
import aslan.aslanov.prayerapp.model.prayerCurrent.Timings
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsEntity
import aslan.aslanov.prayerapp.model.surahs.Data
import aslan.aslanov.prayerapp.model.surahs.SurahEntity
import aslan.aslanov.prayerapp.network.NetworkResult
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "prayerapp.util"
val currentDate: Calendar = Calendar.getInstance()
private lateinit var alarmTime: Calendar
fun logApp(msg: String) {
    Log.d(TAG, "logApp: $msg")
}

fun <T> getServerError(error: ResponseBody?, onCatchError: (NetworkResult<T>) -> Unit) {
    try {
        val jsonError = error?.let {
            JSONObject(it.string())
        }
        jsonError?.let {
            val status = jsonError.getString("status")
            val message = jsonError.getString("data")
            onCatchError(NetworkResult.error("$status  : $message"))
        }


    } catch (e: Exception) {
        onCatchError(NetworkResult.error(e.message))
    }

}

@SuppressLint("SimpleDateFormat")
fun calculateTime(
    startDate: String,
): Calendar {
    try {
        alarmTime = Calendar.getInstance()
        val format = SimpleDateFormat("HH:mm")
        format.parse(startDate)?.let {
            alarmTime.time = it
            alarmTime.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
            alarmTime.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
            alarmTime.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))
            alarmTime.set(Calendar.DATE, currentDate.get(Calendar.DATE))
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return alarmTime
}

@SuppressLint("SimpleDateFormat")
fun calculateNextTime(
    startDate: Calendar,
): Calendar {
    val calendar = Calendar.getInstance()
    try {
        calendar.time = startDate.time
        calendar.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
        calendar.add(Calendar.DATE, 1)
        Log.d("RemainingTimeFragment", "bindUI:  ${calendar.time}")

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return calendar
}

@SuppressLint("SimpleDateFormat")
fun timeDifference(
    currentDate: Calendar,
    stopDate: Calendar,
    onComplete: (elapsedHours: Long, elapsedMinutes: Long, elapsedSeconds: Long) -> Unit
) {
    try {
        val dates = SimpleDateFormat("HH:mm")
        val dateStart = currentDate.time
        val dateStop = stopDate.time
        val different = dateStop.time - dateStart.time

        val secondsInMilli = 1000
        val minutesInMilli = secondsInMilli * 60;
        val hoursInMilli = minutesInMilli * 60;
        val daysInMilli = hoursInMilli * 24;

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


fun Context.makeToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}