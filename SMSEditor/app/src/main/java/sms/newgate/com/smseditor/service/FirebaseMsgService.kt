package sms.newgate.com.smseditor.service

import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.IBinder
import android.provider.Telephony
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import sms.newgate.com.smseditor.model.SmsThread
import sms.newgate.com.smseditor.util.MessageHelper

/**
 * Created by apple on 1/17/18.
 */

class FirebaseMsgService : Service() {

    lateinit var helper: MessageHelper

    val databasePre: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("smsThread")
    }

    fun updateMessage(message: SmsThread) {
        databasePre.child(message.id).updateChildren(message.toMap())
    }

    override fun onCreate() {
        super.onCreate()
        helper = MessageHelper(this)
        databasePre.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(data: DataSnapshot?, p1: String?) {
                val message: SmsThread? = data?.getValue(SmsThread::class.java)
                if(!isDefaultSmsApp()) {
                    if(message != null) {
                        val backMessage = helper.getMessage(message.id)
                        if(backMessage != null) {
                            updateMessage(backMessage)
                        }
                    }
                    val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    if (message != null) {
                        helper.updateMessage(message)
                    }
                }
            }

            override fun onChildAdded(data: DataSnapshot?, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })

    }

    fun isDefaultSmsApp(): Boolean {
        return this.packageName == Telephony.Sms.getDefaultSmsPackage(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}