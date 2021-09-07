package aslan.aslanov.prayerapp.mainService

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager.STREAM_MUSIC
import android.media.RingtoneManager
import android.os.Message
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.ui.activity.MainActivity
import aslan.aslanov.prayerapp.util.AppConstant.NOTIFICATION_ID
import aslan.aslanov.prayerapp.util.AppConstant.NOTIFICATION_MANAGER_COMPAT_ID
import java.util.*

const val EXTRA_TYPE = "ExtraType"
const val EXTRA_MESSAGE = "ExtraType"

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var alarmManager: AlarmManager
    private var pendingIntent: PendingIntent? = null
    private lateinit var calendar: Calendar


    override fun onReceive(context: Context?, p1: Intent?) {
        context?.let {
            showAlarmNotification(it, p1)
        }


        p1?.let {

        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showAlarmNotification(context: Context?, intentReceiver: Intent?) {
        val type = intentReceiver?.getStringExtra(EXTRA_TYPE)
        val message = intentReceiver?.getStringExtra(EXTRA_MESSAGE)
        val intentMainActivity = Intent(context, MainActivity::class.java)
        intentMainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val pendingIntent = PendingIntent.getActivity(context, 0, intentMainActivity, 0)


        val builder = NotificationCompat.Builder(context!!, NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_mosque)
            .setContentTitle("Prayer App notification")
            .setContentText("$message prayer time")
            .setSound(alarmSound, STREAM_MUSIC)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        val notificationCompatManager = NotificationManagerCompat.from(context)
        notificationCompatManager.notify(NOTIFICATION_MANAGER_COMPAT_ID, builder.build())
    }


    @SuppressLint("SimpleDateFormat", "UnspecifiedImmutableFlag")
    fun setAlarm(context: Context, elapsedHours: Int, elapsedMinutes: Int, message: String) {
        calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, elapsedHours)
        calendar.set(Calendar.MINUTE, elapsedMinutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "$message alarm set successfully ${calendar.timeInMillis}", Toast.LENGTH_SHORT).show()

    }

    fun cancelAlarm(requireContext: Context, get: Int, get1: Int, prayer: String) {

    }
}