package sms.newgate.com.smseditor.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.text.TextUtils

/**
 * Created by apple on 1/16/18.
 */
class TelephoneUtil {

    companion object {
        val uri = Uri.parse("content://sms")
        val tag = TelephoneUtil.javaClass.simpleName
        val CONTACT_PROJ = arrayOf("_id", "display_name", "normalized_number", "photo_uri", "photo_thumb_uri")
        val PROFILE_PROJ = arrayOf("_id", "display_name", "photo_thumb_uri")

        fun getContactProfile(context: Context, number: String): Cursor? {
            if(TextUtils.isEmpty(number)) {
                return null
            }
            val contentResolver = context.contentResolver
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
            return contentResolver.query(uri, null, null, null, null)
        }

        fun getOwnProfile(context: Context): Cursor? {
            return getContactProfile(context, getOwnNumber(context))
        }

        fun getOwnNumber(context: Context): String {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.line1Number
        }

        fun getOwnPhoto(context: Context): String {
            val cr = context.contentResolver
            val cursor = cr.query(ContactsContract.Profile.CONTENT_URI, PROFILE_PROJ, null, null, null)

            return CursorInspector.getContentByColumnIndex(cursor, 2)
        }

    }

    var context: Context? = null

    fun TelephoneUtil(context: Context) {
        this.context = context
    }
}