package sms.newgate.com.smseditor.util

import android.content.Context
import sms.newgate.com.smseditor.model.SmsThread

/**
 * Created by apple on 2/9/18.
 */
class AppStore {

    companion object {
        var instanceAppStore: AppStore? = null
        fun getInstance(): AppStore {
            if(instanceAppStore == null) {
                instanceAppStore = AppStore()
            }
            return instanceAppStore!!
        }
    }

    var smsThreads: ArrayList<SmsThread> = arrayListOf()
}