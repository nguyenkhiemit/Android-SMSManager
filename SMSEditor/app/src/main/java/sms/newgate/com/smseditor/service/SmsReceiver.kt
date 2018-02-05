package sms.newgate.com.smseditor.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.telephony.SmsMessage
import android.util.Log
import kotlinx.android.synthetic.main.msg_thread_item_layout.view.*
import sms.newgate.com.smseditor.R
import sms.newgate.com.smseditor.util.MessageHelper

/**
 * Created by apple on 1/20/18.
 */
class SmsReceiver : BroadcastReceiver() {

    val TAG: String = javaClass.simpleName

    lateinit var helper: MessageHelper

    override fun onReceive(context: Context, intent: Intent) {
        helper = MessageHelper(context)
        val extras = intent.extras
        if(extras != null) {
            val smsextras = extras.get("pdus") as Array<Any>

            for (i in smsextras.indices) {
                val smsmsg = SmsMessage.createFromPdu(smsextras[i] as ByteArray)

                val body = smsmsg.messageBody.toString()
                val address = smsmsg.originatingAddress.toString()
                helper.putSmsToDatabase(smsmsg)
                sendNotification(context, address, body)
            }

        }
    }

    fun sendNotification(context: Context, address: String, body: String) {
        val builder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_avatar)
                .setContentTitle(address)
                .setContentText(body)
        val mNotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(100, builder.build())
    }

}
