package sms.newgate.com.smseditor.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Created by apple on 1/16/18.
 */
class ActivityHelper {

    companion object {
        val tag = ActivityHelper.javaClass.simpleName

        fun startAndFinish(activity: Activity, clazz: Class<*>, finish: Boolean) {
            start(activity, clazz)
            if(finish) {
                activity.finish()
            }
        }

        fun start(activity: Activity, clazz: Class<*>) {
            val intent = Intent(activity, clazz)
            activity.startActivity(intent)
        }

        fun startWithBundle(activity: Activity, clazz: Class<*>, bundle: Bundle, finish: Boolean) {
            var intent = Intent(activity, clazz)
            intent.putExtras(bundle)
            activity.startActivity(intent)
            if(finish) {
                activity.finish()
            }
        }

    }
}