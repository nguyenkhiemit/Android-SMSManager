package sms.newgate.com.smseditor.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import sms.newgate.com.smseditor.model.SmsThread

/**
 * Created by apple on 1/17/18.
 */

class FirebaseMsgService : Service() {

    val databasePre: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("smsThread")
    }

    override fun onCreate() {
        super.onCreate()
        databasePre.addValueEventListener(ValueListener(this))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    class ValueListener(val context: Context): ValueEventListener {

        override fun onCancelled(error: DatabaseError?) {

        }

        override fun onDataChange(data: DataSnapshot?) {
            val smsThread = data?.getValue(SmsThread::class.java)
            EventBus.getDefault().post(smsThread)
        }

    }

}