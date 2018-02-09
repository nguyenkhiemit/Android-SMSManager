package sms.newgate.com.smseditorremote.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by apple on 2/6/18.
 */

fun String.convertToDate() : Date? {
    try {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.parse(this)
    } catch (e: Exception) {

    }
    return null
}

fun Date.getDateYear(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.YEAR)
}

fun Date.getDateMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH)
}

fun Date.getDateDay(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.getDateHour(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.HOUR)
}

fun Date.getDateMiniute(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MINUTE)
}

fun Date.getDateSecond(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.SECOND)
}