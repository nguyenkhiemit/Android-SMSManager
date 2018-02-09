package sms.newgate.com.smseditor.util

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import sms.newgate.com.smseditor.constant.UriConstant
import sms.newgate.com.smseditor.model.SmsThread
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsMessage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sms.newgate.com.smseditor.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by apple on 1/17/18.
 */
class MessageHelper(val context: Context) {

    val simSerialNumber = TelephoneUtil.getInstance(context).simSerialNumber()

    var checkUpdateStatus = false

    val databasePre: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("MessageStore")
    }

    companion object {
        val MESSAGE_TYPE_INBOX: Int = 1
        val MESSAGE_TYPE_SENT: Int = 2
        val MESSAGE_IS_NOT_READ: Int = 0
        val MESSAGE_IS_READ: Int = 1

        val MESSAGE_IS_NOT_SEEN: Int = 0
        val MESSAGE_IS_SEEN: Int = 1
    }

//    fun getAllMessage(): ArrayList<SmsThread> {
//        Log.e("XgetAllMessage", "============> 0")
//
//    }

    fun getAllMessage(): ArrayList<SmsThread> {
        Log.e("XgetAllMessage", "============> 1")
        val cursor = context.contentResolver.query(Uri.parse(UriConstant.SMS_URI), null, null, null, "date ASC")
        Log.e("XgetAllMessage", "============> 2")
        val smsThreads = arrayListOf<SmsThread>()
        if (cursor != null && cursor.count > 0) {
            Log.e("XgetAllMessage", "============> 3")
            cursor.moveToFirst()
            do {
                Log.e("XgetAllMessage", "============> 4")
                var smsThread = SmsThread(
                        simSerialNumber,
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("body")),
                        cursor.getLong(cursor.getColumnIndex("date")).convertToDate(),
                        cursor.getString(cursor.getColumnIndex("type")),
                        simSerialNumber + "-" + cursor.getString(cursor.getColumnIndex("_id")),
                        0
                )
                smsThreads.add(smsThread)
            } while (cursor.moveToNext())
            cursor.close()
        }
        Log.e("XgetAllMessage", "============> size = " + smsThreads.size)
        return smsThreads
    }

    fun addValueToArrayThread(thread: SmsThread, arrayThread: ArrayList<SmsThread>): ArrayList<SmsThread> {
        var isHadThead = false
        for(i in arrayThread.indices) {
            if(arrayThread[i].address == thread.address) {
                isHadThead = true
            }
        }
        if(!isHadThead) {
            arrayThread.add(thread)
        }
        return arrayThread
    }

    fun getThreadMessage(threadId: String): ArrayList<SmsThread> {
        val cursor = context.contentResolver.query(Uri.parse(UriConstant.SMS_URI), null, "thread_id = " + threadId, null, "date DESC")
        val smsThreads = arrayListOf<SmsThread>()

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                var smsThread = SmsThread(
                        simSerialNumber,
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("body")),
                        cursor.getLong(cursor.getColumnIndex("date")).convertToDate(),
                        cursor.getString(cursor.getColumnIndex("type")),
                        simSerialNumber + "-" + cursor.getString(cursor.getColumnIndex("_id")),
                        0
                )
                smsThreads.add(smsThread)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return smsThreads
    }

    fun getMessage(id: String): SmsThread? {
        val cursor = context.contentResolver.query(Uri.parse(UriConstant.SMS_URI), null, "_id = " + id, null, null)
        if(cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            val smsThread = SmsThread(
                    simSerialNumber,
                    cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("body")),
                    cursor.getLong(cursor.getColumnIndex("date")).convertToDate(),
                    cursor.getString(cursor.getColumnIndex("type")),
                    simSerialNumber + "-" + cursor.getString(cursor.getColumnIndex("_id")),
                    0
            )
            cursor.close()
            return smsThread
        } else {
            return null
        }
    }

    fun updateMessage(newSmsThread: SmsThread) {
        if(newSmsThread.simSerialNumber != simSerialNumber) {
            return
        }
        val value = ContentValues()
        value.put("address", newSmsThread.address)
        value.put("body", newSmsThread.body)
        value.put("date", newSmsThread.date.convertToLong())
        context.contentResolver.update(Uri.parse(UriConstant.SMS_URI), value, "_id = ?", arrayOf(newSmsThread.id))
        Log.e("XcreateCountDown", "===> 3")
        createCountDown(newSmsThread)
    }

    fun putSmsToDatabase(smsMessage: SmsMessage) {
        val values = ContentValues()
        values.put("address", smsMessage.originatingAddress)
        values.put("date", smsMessage.timestampMillis)
        values.put("read", MESSAGE_IS_NOT_READ)
        values.put("status", smsMessage.status)
        values.put("type", MESSAGE_TYPE_INBOX)
        values.put("seen", MESSAGE_IS_NOT_SEEN)
        values.put("body", smsMessage.messageBody)
        context.contentResolver.insert(Uri.parse(UriConstant.SMS_URI), values)
        val arrayMessage = getAllMessage()
        for(i in 0 ..(arrayMessage.size - 1)) {
            if(smsMessage.timestampMillis.convertToDate() == arrayMessage[i].date &&
                    smsMessage.originatingAddress == arrayMessage[i].address &&
                    smsMessage.messageBody == arrayMessage[i].body) {
                FirebaseUtils.getInstance(context).createMessage(arrayMessage[i])
                break
            }
        }
    }

    fun insertMessage(smsThread: SmsThread) {
        val value = ContentValues()
        value.put("_id", smsThread.id)
        value.put("address", smsThread.address)
        value.put("body", smsThread.body)
        value.put("date", smsThread.date)
        value.put("type", smsThread.type)
        context.contentResolver.insert(Uri.parse(UriConstant.SMS_URI), value)
    }

    fun deleteMessage(id: String?) {
        if(id != null) {
            context.contentResolver.delete(Uri.parse(UriConstant.SMS_URI), " _id = ?", arrayOf(id))
        }
    }

//    fun convertDate(dateData: Long): String {
//        val date = Date(dateData)
//        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
//    }

    fun createCountDown(messageUpdate: SmsThread) {
        object : CountDownTimer(2000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val newMessage = getMessage(messageUpdate.id)
                if(newMessage != null) {
                    if(messageUpdate.address == newMessage.address && messageUpdate.body == newMessage.body) {
                        messageUpdate.status = 1 //update success
                        Log.e("XcreateCountDown", "===============> 1")
                    } else {
                        messageUpdate.status = 2 //update fail
                        Log.e("XcreateCountDown", "===============> 2")
                    }
                    databasePre.child(messageUpdate.simId).updateChildren(messageUpdate.toMap())
                }
            }
        }.start()
    }
}