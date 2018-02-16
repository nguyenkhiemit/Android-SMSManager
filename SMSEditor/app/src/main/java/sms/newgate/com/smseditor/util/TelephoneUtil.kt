package sms.newgate.com.smseditor.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.util.Log


/**
 * Created by apple on 1/16/18.
 */
class TelephoneUtil(val context: Context) {

    companion object {
        var instanceTelephoneUtil: TelephoneUtil? = null
        fun getInstance(context: Context): TelephoneUtil {
            if(instanceTelephoneUtil == null) {
                instanceTelephoneUtil = TelephoneUtil(context)
            }
            return instanceTelephoneUtil!!
        }

    }

    var simSerialNumber: String = ""

//    fun simSerialNumber(): String {
//        if(context == null)
//            return ""
//        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE ) == PackageManager.PERMISSION_GRANTED ) {
//            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            return telephonyManager.simSerialNumber
//        }
//        return ""
//    }
}