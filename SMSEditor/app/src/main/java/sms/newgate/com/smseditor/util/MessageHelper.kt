package sms.newgate.com.smseditor.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import sms.newgate.com.smseditor.constant.UriConstant
import sms.newgate.com.smseditor.model.SmsThread

/**
 * Created by apple on 1/17/18.
 */
class MessageHelper(val context: Context) {

    fun getAllMessage(): ArrayList<SmsThread> {
        val cursor = context.contentResolver.query(Uri.parse(UriConstant.SMS_URI), null, null, null, null)
        val smsThreads = arrayListOf<SmsThread>()

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                var smsThread = SmsThread(
                        cursor.getString(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("thread_id")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("body")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getString(cursor.getColumnIndex("type"))
                )
                Log.e("XgetAllMessage", "body ====> " + smsThread.body)
                smsThreads.add(smsThread)
            } while (cursor.moveToNext())
            cursor.close()
        }

        var arrayThread = arrayListOf<SmsThread>()
        for(i in smsThreads.indices) {
            arrayThread = addValueToArrayThread(smsThreads[i], arrayThread)
        }
        return arrayThread
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
}