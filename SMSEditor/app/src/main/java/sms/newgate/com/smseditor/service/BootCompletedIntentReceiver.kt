package sms.newgate.com.smseditor.service

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


/**
 * Created by apple on 1/30/18.
 */
class BootCompletedIntentReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            val pushIntent = Intent(context, FirebaseMsgService::class.java)
            context.startService(pushIntent)
        }
    }
}