package sms.newgate.com.smseditor.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import sms.newgate.com.smseditor.constant.UriConstant
import sms.newgate.com.smseditor.model.SmsThread
import android.content.ContentResolver
import android.telephony.SmsMessage
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by apple on 1/17/18.
 */
class MessageHelper(val context: Context) {

    val simSerialNumber = TelephoneUtil.getInstance(context).simSerialNumber()

    companion object {
        val MESSAGE_TYPE_INBOX: Int = 1
        val MESSAGE_TYPE_SENT: Int = 2
        val MESSAGE_IS_NOT_READ: Int = 0
        val MESSAGE_IS_READ: Int = 1

        val MESSAGE_IS_NOT_SEEN: Int = 0
        val MESSAGE_IS_SEEN: Int = 1
    }

    fun getAllMessage(): ArrayList<SmsThread> {
        val cursor = context.contentResolver.query(Uri.parse(UriConstant.SMS_URI), null, null, null, "date ASC")
        val smsThreads = arrayListOf<SmsThread>()
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                var smsThread = SmsThread(
                        simSerialNumber,
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("thread_id")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("body")),
                        convertDate(cursor.getLong(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("type"))
                )
                smsThreads.add(smsThread)
            } while (cursor.moveToNext())
            cursor.close()
        }
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
                        cursor.getString(cursor.getColumnIndex("thread_id")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("body")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("type"))
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
                    cursor.getString(cursor.getColumnIndex("thread_id")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("body")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("type"))
            )
            cursor.close()
            return smsThread
        } else {
            return null
        }
    }

    fun updateMessage(newSmsThread: SmsThread) {
//        deleteMessage(newSmsThread.id)
//        insertMessage(newSmsThread)
        val value = ContentValues()
        value.put("address", newSmsThread.address)
        value.put("body", newSmsThread.body)
        context.contentResolver.update(Uri.parse(UriConstant.SMS_URI), value, "_id = ?", arrayOf(newSmsThread.id))
        Log.e("XupdateMessage", "====> updateMessage!!!!!")
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
            if(convertDate(smsMessage.timestampMillis) == arrayMessage[i].date &&
                    smsMessage.originatingAddress == arrayMessage[i].address &&
                    smsMessage.messageBody == arrayMessage[i].body) {
                FirebaseUtils.getInstance(context).createMessage(arrayMessage[i])
                Log.e("XputSmsToDatabase", "" + arrayMessage[i].body)
                break
            }
        }
    }

    fun insertMessage(smsThread: SmsThread) {
        val value = ContentValues()
        value.put("_id", smsThread.id)
        value.put("thread_id", smsThread.threadId)
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

    fun convertDate(dateData: Long): String {
        val date = Date(dateData)
        return SimpleDateFormat("MM-dd-yyyy hh:mm:ss").format(date)
    }
}