package aslan.aslanov.prayerapp.mainService

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import aslan.aslanov.prayerapp.util.timeDifference
import java.util.*

private const val TAG = "AlarmNotification"

