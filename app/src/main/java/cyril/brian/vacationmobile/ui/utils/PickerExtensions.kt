package cyril.brian.vacationmobile.ui.utils

import android.widget.DatePicker
import android.widget.TimePicker

fun DatePicker.getStringDate(): String {
    return String.format("%02d/%02d/%d", this.dayOfMonth, (this.month + 1), this.year)
}
fun TimePicker.getStringTime(): String {
    return String.format("%02d:%02d", this.hour, this.minute)
}