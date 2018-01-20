package sms.newgate.com.smseditor.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import sms.newgate.com.smseditor.model.SmsThread

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
        FirebaseDatabase.getInstance().getReference("smsThread")
    }

    fun createMessages(smsThreads: ArrayList<SmsThread>) {
        for(i in smsThreads.indices) {
            val id = databasePre.push().key
            val message = smsThreads[0]
            databasePre.child(id).setValue(message)
        }
    }

}