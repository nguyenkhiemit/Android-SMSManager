package sms.newgate.com.smseditor.util

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import sms.newgate.com.smseditor.model.SmsThread
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by apple on 1/18/18.
 */
class FirebaseUtils(val context: Context) {

    companion object {
        var instanceFirebase: FirebaseUtils? = null
        fun getInstance(context: Context): FirebaseUtils {
            if(instanceFirebase == null) {
                instanceFirebase = FirebaseUtils(context)
            }
            return instanceFirebase!!
        }
    }

    val databasePre: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("MessageStore")
    }

    fun createMessages(smsThreads: ArrayList<SmsThread>) {
        for(i in smsThreads.indices) {
            val message = smsThreads[i]
            if(message != null && !TextUtils.isEmpty(message.address)) {
                    databasePre.child(message.simId).setValue(message)
            }
        }
    }

    fun createMessage(message: SmsThread) {
        if(message != null && !TextUtils.isEmpty(message.address)) {
            Log.e("XcreateMessage", "=====> new message")
            databasePre.child(message.simId).setValue(message)
        }
    }

}