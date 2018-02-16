package sms.newgate.com.smseditor.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.IBinder
import android.provider.Telephony
import android.util.Log
import com.google.firebase.database.*
import sms.newgate.com.smseditor.model.SmsThread
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import sms.newgate.com.smseditor.constant.UriConstant
import android.net.ConnectivityManager
import android.content.IntentFilter
import sms.newgate.com.smseditor.util.*
import sms.newgate.com.smseditorremote.utils.PrefsUtil


/**
 * Created by apple on 1/17/18.
 */

class FirebaseMsgService : Service() {

    lateinit var helper: MessageHelper

    var startTimer = false

    val databasePre: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("MessageStore")
    }

    fun updateMessage(message: SmsThread) {
        databasePre.child(message.simId).updateChildren(message.toMap())
    }

    override fun onCreate() {
        super.onCreate()
        startCheckNewMessage()
//        createReceiveMessage()
        helper = MessageHelper(this)
        val simSerialNumber = PrefsUtil.getInstance(this).getPref<String>(Constant.SIM_SERIAL_NUMBER)
        databasePre.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(data: DataSnapshot?, p1: String?) {
//                updateNewMessage()
                if(!startTimer) {
                    startTimer = true
                    startTimer()
                }

                val message: SmsThread? = data?.getValue(SmsThread::class.java)
                if(message == null)
                    return
                if(message.simSerialNumber != simSerialNumber) {
                    return
                }
                val currentMessage = helper.getMessage(message.id)
                Log.e("XcreateCountDown", "===============> " + message.date)
                Log.e("XcreateCountDown", "===============> " + currentMessage?.date)
                if(currentMessage != null) {
                    if(message.address == currentMessage.address && message.body == currentMessage.body
                            && message.date == currentMessage.date) {
                        return
                    }
                }
                Log.e("XcreateCountDown", "===> update")
                if(!isDefaultSmsApp()) {
                    if(message != null) {
                        if(currentMessage != null) {
                            currentMessage.status = 2 //update fail
                            updateMessage(currentMessage)
                        }
                    }
                    intentToMessageDefault()
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
        var mTimeToGo: Long = 1000000 * 1000
        val mCountDownTimer = object : CountDownTimer(mTimeToGo, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(!isDefaultSmsApp()) {
                    mTimeToGo -= 1
                    Log.e("XtimeCount", "" + mTimeToGo)
                    intentToMessageDefault()
                } else {
                    Log.e("XtimeCount", "====> stop")
                    startTimer = false
                    cancel()
                }
            }

            override fun onFinish() {
            }
        }.start()
    }

    private fun startCheckNewMessage() {
        var mTimeToGo: Long = 1000000 * 1000
        val mCountDownTimer = object : CountDownTimer(mTimeToGo, 5000) {
            override fun onTick(millisUntilFinished: Long) {
                updateNewMessage()
            }

            override fun onFinish() {
            }
        }.start()
    }

    fun intentToMessageDefault() {
        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun updateNewMessage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE ) == PackageManager.PERMISSION_GRANTED) {
            var arrayCheckMessage = helper.getAllMessage()
//            var currentArrayMessage = AppStore.getInstance().smsThreads
//            var arrayNewMessage = arrayListOf<SmsThread>()
//            for(i in arrayCheckMessage.indices) {
//                if(!currentArrayMessage.contains(arrayCheckMessage[i])) {
//                    Log.e("updateNewMessage", "====> new!!!")
//                    arrayNewMessage.add(arrayCheckMessage[i])
//                }
//            }
            Log.e("XupdateNewMessage", "======> update new message")
            FirebaseUtils.getInstance(this).createMessages(arrayCheckMessage)
        }
    }

    fun checkIsNewMessage() {
        
    }

    fun createReceiveMessage() {
        val br = SmsReceiver()
        val filter = IntentFilter()
        filter.addAction(Telephony.Sms.Intents.SMS_DELIVER_ACTION)
        this.registerReceiver(br, filter)
    }

}