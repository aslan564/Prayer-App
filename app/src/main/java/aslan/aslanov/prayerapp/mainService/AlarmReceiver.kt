package aslan.aslanov.prayerapp.mainService

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager.STREAM_MUSIC
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.model.prayerCurrent.TimingsConverted
import aslan.aslanov.prayerapp.model.whereWereWe.AyahsOrSurah
import aslan.aslanov.prayerapp.ui.activity.MainActivity
import aslan.aslanov.prayerapp.util.AppConstant.NOTIFICATION_ID
import aslan.aslanov.prayerapp.util.AppConstant.NOTIFICATION_MANAGER_AYAH_ID
import aslan.aslanov.prayerapp.util.AppConstant.NOTIFICATION_MANAGER_HADEETHS_ID
import aslan.aslanov.prayerapp.util.AppConstant.NOTIFICATION_MANAGER_PRAYER_ID
import aslan.aslanov.prayerapp.util.AppConstant.NOTIFICATION_MANAGER_SALAWAT_ID
import aslan.aslanov.prayerapp.util.PendingRequests.CATCH_REQUEST_CODE_FROM_MAIN
import aslan.aslanov.prayerapp.util.PendingRequests.REQUEST_CODE_PRAYER_TIME
import java.lang.Exception
import java.util.*

const val EXTRA_TYPE = "EXTRA_TYPE"
const val EXTRA_MESSAGE = "EXTRA_MESSAGE"

class AlarmReceiver : BroadcastReceiver() {
    private var alarmManager: AlarmManager? = null


    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            showAlarmNotification(it, intent, REQUEST_CODE_PRAYER_TIME)
        }

    }


    fun cancelAlarm(pendingIntent: PendingIntent) {
        alarmManager?.cancel(pendingIntent)
    }

    companion object {
        @SuppressLint("UnspecifiedImmutableFlag")
        fun showAlarmNotification(context: Context?, intent: Intent?, requestCode: Int) {
            try {
                var messageTitle = ""
                var messageChannelId = 0
                val type = intent?.getStringExtra(EXTRA_TYPE)
                if (type != null) {
                     when (type) {
                        AyahsOrSurah.AYAHS.name -> {
                            messageTitle =  type
                            messageChannelId=NOTIFICATION_MANAGER_AYAH_ID
                        }
                        AyahsOrSurah.HADEETHS.name -> {
                            messageTitle =  type
                            messageChannelId= NOTIFICATION_MANAGER_HADEETHS_ID
                        }
                        AyahsOrSurah.SALAWAT.name -> {
                            messageTitle = type
                            messageChannelId= NOTIFICATION_MANAGER_SALAWAT_ID
                        }
                        else -> {
                            messageTitle =  "$type time"
                            messageChannelId= NOTIFICATION_MANAGER_PRAYER_ID
                        }
                    }

                }

                val message = intent?.getStringExtra(EXTRA_MESSAGE)
                val intentMainActivity = Intent(context, MainActivity::class.java)
                intentMainActivity.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intentMainActivity.putExtra(CATCH_REQUEST_CODE_FROM_MAIN, requestCode)
                val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)


                val pendingIntent = PendingIntent.getActivity(
                    context,
                    requestCode,
                    intentMainActivity,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val builder = NotificationCompat.Builder(context!!, NOTIFICATION_ID)
                    .setSmallIcon(R.drawable.ic_mosque)
                    .setContentTitle(messageTitle)
                    .setContentText("$message ")
                    .setSound(alarmSound, STREAM_MUSIC)
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400))
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                val notificationCompatManager = NotificationManagerCompat.from(context)
                notificationCompatManager.notify(messageChannelId, builder.build())

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun setAlarmManager(prayer: TimingsConverted, context: Context) {
            val alarmReceiver = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmTime = Calendar.getInstance()
            alarmTime.time = prayer.prayerTime
            alarmReceiver.set(
                AlarmManager.RTC_WAKEUP,
                alarmTime.timeInMillis,
                pendingIntentCreator(prayer, context)
            )
            Toast.makeText(context, "alarm added ${prayer.prayerName}", Toast.LENGTH_SHORT).show()
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun pendingIntentCreator(prayer: TimingsConverted, context: Context): PendingIntent {
            val pendingRequestCode = Random().nextInt()
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(EXTRA_TYPE, prayer.prayerName.name.lowercase(Locale.getDefault()))
            intent.putExtra(
                EXTRA_MESSAGE,
                prayer.prayerTime.toString().lowercase(Locale.getDefault())
            )
            return PendingIntent.getBroadcast(
                context,
                pendingRequestCode,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }
    }
}