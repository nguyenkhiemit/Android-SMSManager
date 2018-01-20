package sms.newgate.com.smseditor.util

import android.database.Cursor
import android.util.Log

/**
 * Created by apple on 1/16/18.
 */
class CursorInspector {
    companion object {
        internal val tag = CursorInspector::class.java.simpleName

        fun printFirstEntry(cursor: Cursor?) {
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val cols = cursor.columnCount

                for (i in 0 until cols) {
                    Log.i(tag, i.toString() + " " + cursor.getColumnName(i) + " : " + cursor.getString(i))
                }
            }
        }

        fun printContent(cursor: Cursor?) {
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val cols = cursor.columnCount

                do {
                    for (i in 0 until cols) {
                        Log.i(tag, i.toString() + " " + cursor.getColumnName(i) + " : " + cursor.getString(i))
                    }
                } while (cursor.moveToNext())

                cursor.close()
            }
        }

        // applicable to cursor with single entry
        fun getContentByColumnIndex(cursor: Cursor, index: Int): String {
            cursor.moveToFirst()

            return cursor.getString(index)
        }
    }
}