package sms.newgate.com.smseditor.misc

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by apple on 1/17/18.
 */
class HandlessSmsSendService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}