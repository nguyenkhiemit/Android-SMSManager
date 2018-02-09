package sms.newgate.com.smseditorremote.utils

/**
 * Created by apple on 2/7/18.
 */
class Utils {
    fun getYearFromDate(date: String): String {
        val dateArray = date.split(" ")
        val timeArray = dateArray[0].split("-")
        return timeArray[0]
    }

    fun getMonthFromDate(date: String): String {
        val dateArray = date.split(" ")
        val timeArray = dateArray[0].split("-")
        return timeArray[1]
    }

    fun getDayFromDate(date: String): String {
        val dateArray = date.split(" ")
        val timeArray = dateArray[0].split("-")
        return timeArray[2]
    }

    fun getHourFromDate(date: String): String {
        val dateArray = date.split(" ")
        val timeArray = dateArray[1].split(":")
        return timeArray[0]
    }

    fun getMinuteFromDate(date: String): String {
        val dateArray = date.split(" ")
        val timeArray = dateArray[1].split(":")
        return timeArray[1]
    }

    fun getSecondFromDate(date: String): String {
        val dateArray = date.split(" ")
        val timeArray = dateArray[1].split(":")
        return timeArray[2]
    }
}