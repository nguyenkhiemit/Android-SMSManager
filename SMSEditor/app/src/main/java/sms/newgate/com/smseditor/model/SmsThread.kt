package sms.newgate.com.smseditor.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by apple on 1/17/18.
 */
@IgnoreExtraProperties
class SmsThread: Parcelable {

    var id: String? = ""
    var threadId: String? = ""
    var address: String? = ""
    var body: String? = ""
    var date: String? = ""
    var type: String? = ""

    constructor()

    constructor(id: String,
                threadId: String,
                address: String,
                body: String,
                date: String,
                type: String) {
        this.id = id
        this.threadId = threadId
        this.address = address
        this.body = body
        this.date = date
        this.type = type
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(threadId)
        parcel.writeString(address)
        parcel.writeString(body)
        parcel.writeString(date)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SmsThread> {
        override fun createFromParcel(parcel: Parcel): SmsThread {
            return SmsThread(parcel)
        }

        override fun newArray(size: Int): Array<SmsThread?> {
            return arrayOfNulls(size)
        }
    }

}
