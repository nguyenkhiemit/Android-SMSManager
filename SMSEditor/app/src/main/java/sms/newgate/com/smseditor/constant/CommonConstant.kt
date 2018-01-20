package sms.newgate.com.smseditor.constant

import java.text.SimpleDateFormat

/**
 * Created by apple on 1/17/18.
 */
class CommonConstant {
    companion object {
        val THREAD_ID = "threadId"
        val APP_NS = "com.rayy.android.editad"
        val OTHER_PHOTO = "otherPhoto"
        val OWN_PHOTO = "ownPhoto"
        val DATE = "date"
        val SENDER = "sender"
        val BODY = "body"
        val MSG_ID = "msgId"
        val TYPE = "type"
        var dateFormat = SimpleDateFormat("HH:mm yyyy-MM-dd")
    }
}