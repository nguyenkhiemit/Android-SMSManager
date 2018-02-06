package sms.newgate.com.smseditorremote.model

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
    var simId: String = ""
    var status: Int = 0

    constructor(parcel: Parcel) : this() {
        simSerialNumber = parcel.readString()
        id = parcel.readString()
        threadId = parcel.readString()
        address = parcel.readString()
        body = parcel.readString()
        date = parcel.readString()
        type = parcel.readString()
        simId = parcel.readString()
        status = parcel.readInt()
    }

    constructor()

    constructor(simSerialNumber: String,
                id: String,
                threadId: String,
                address: String,
                body: String,
                date: String,
                type: String,
                simId: String,
                status: Int) {
        this.simSerialNumber = simSerialNumber
        this.id = id
        this.threadId = threadId
        this.address = address
        this.body = body
        this.date = date
        this.type = type
        this.simId = simId
        this.status = status
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
        result.put("simId", simId)
        result.put("status", status)
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(simSerialNumber)
        parcel.writeString(id)
        parcel.writeString(threadId)
        parcel.writeString(address)
        parcel.writeString(body)
        parcel.writeString(date)
        parcel.writeString(type)
        parcel.writeString(simId)
        parcel.writeInt(status)
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

}
