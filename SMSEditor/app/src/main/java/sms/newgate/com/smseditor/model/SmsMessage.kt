package sms.newgate.com.smseditor.model

/**
 * Created by apple on 1/17/18.
 */
data class SmsMessage(
    var msgId: Int,
    var threadId: Int,
    var type: Int,
    var addr: String,
    var msg: String,
    var timestamp: Long
)
