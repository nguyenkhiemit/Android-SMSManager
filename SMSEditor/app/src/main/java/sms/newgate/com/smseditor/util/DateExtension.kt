package sms.newgate.com.smseditor.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by apple on 2/7/18.
 */

fun Long.convertToDate(): String {
    try {
        val date = Date(this)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
    } catch (e: Exception) {

    }
    return ""
}

fun String.convertToLong(): Long {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = sdf.parse(this)
        return date.time
    } catch (e: Exception) {

    }
    return 0
}