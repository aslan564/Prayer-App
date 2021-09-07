package aslan.aslanov.prayerapp.mainService

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import aslan.aslanov.prayerapp.util.timeDifference
import java.util.*

private const val TAG = "AlarmNotification"

class AlarmNotification : Service() {

    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null
    private lateinit var calendar: Calendar


    private var countDownTimer: CountDownTimer? = null
    private var hours: Long = 0
    private var minute: Long = 0
    val intent = Intent(COUNTDOWN_BR)


    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG, "onBind: ")
        return null

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 11)
        calendar.set(Calendar.MINUTE, 37)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        setAlarm(calendar)
    }

    private fun sendIntentExtra(remTime: Long) {
        intent.putExtra(INTENT_SECOND, remTime)
        intent.putExtra(INTENT_HOURS, hours)
        intent.putExtra(INTENT_MINUTE, minute)
        sendBroadcast(intent)
    }


    override fun onDestroy() {
        countDownTimer?.cancel()
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private fun setAlarm(calendar: Calendar) {
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(applicationContext, "alarm set successfully", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val COUNTDOWN_BR = "aslan.aslanov.prayerapp.mainService"
        const val INTENT_SECOND = "second"
        const val INTENT_HOURS = "hours"
        const val INTENT_MINUTE = "minute"
        const val INTENT_TIME_COMPLETE = "countDownComplete"
    }

}