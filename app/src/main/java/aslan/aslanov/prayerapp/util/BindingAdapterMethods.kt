package aslan.aslanov.prayerapp.util

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

@SuppressLint("SetTextI18n")
@BindingAdapter("dateConvertToString")
fun TextView.dateToString(date: Date?) {
    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
        this.text = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
    } else {
        this.text = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
    }
}