package sms.newgate.com.smseditorremote

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by apple on 1/28/18.
 */

@IgnoreExtraProperties
class Message : Parcelable {

    var simSerialNumber: String = ""
    var id: String = ""
    var threadId: String = ""
    var address: String = ""
    var body: String = ""
    var date: String = ""
    var type: String = ""

    constructor()

    constructor(simSerialNumber: String,
                id: String,
            threadId: String,
            address: String,
            body: String,
            date: String,
            type: String) {
        this.simSerialNumber = simSerialNumber
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
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(simSerialNumber)
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

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }

    @Exclude
    fun toMap(): HashMap<String, Any> {
        var result = HashMap<String, Any>()
        result.put("simSerialNumber", simSerialNumber)
        result.put("id", id)
        result.put("thread_id", threadId)
        result.put("address", address)
        result.put("body", body)
        result.put("date", date)
        result.put("type", type)
        return result
    }

}
