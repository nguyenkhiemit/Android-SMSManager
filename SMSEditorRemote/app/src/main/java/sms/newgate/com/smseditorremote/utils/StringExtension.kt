package sms.newgate.com.smseditorremote.utils

/**
 * Created by apple on 2/6/18.
 */

fun String.replaceAddress(): String {
    if(this.contains("+84")) {
        return this.replace("+84", "0")
    } else {
        return this
    }
}