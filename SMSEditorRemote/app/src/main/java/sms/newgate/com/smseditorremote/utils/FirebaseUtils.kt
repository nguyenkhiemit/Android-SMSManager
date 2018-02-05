package sms.newgate.com.smseditorremote.utils

import android.content.Context
import com.google.firebase.database.*
import sms.newgate.com.smseditorremote.model.Message

/**
 * Created by apple on 1/28/18.
 */
class FirebaseUtils(val context: Context) {
    companion object {
        var instanceFirebase: FirebaseUtils? = null
        fun getInstance(context: Context): FirebaseUtils {
            if(instanceFirebase == null) {
                instanceFirebase = FirebaseUtils(context)
            }
            return instanceFirebase!!
        }
    }

    val databasePre: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("MessageStore")
    }

    fun getAllMessage(listener: FirebaseListener) {
        databasePre.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(data: DataSnapshot?, p1: String?) {
                val message: Message? = data?.getValue(Message::class.java)
                if(message != null) {
                    listener.getAllMessageListener(message)
                }
            }

            override fun onChildAdded(data: DataSnapshot?, p1: String?) {
                val message: Message? = data?.getValue(Message::class.java)
                if(message != null) {
                    listener.getAllMessageListener(message)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
    }

    fun getMessageChange(listener: FirebaseChangeListener) {
        databasePre.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(data: DataSnapshot?, p1: String?) {
                val message: Message? = data?.getValue(Message::class.java)
                if(message != null) {
                    listener.getMessageChange(message)
                }
            }

            override fun onChildAdded(data: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
    }

    fun updateMessage(message: Message) {
        databasePre.child(message.simId).updateChildren(message.toMap())
    }

    interface FirebaseListener {
        fun getAllMessageListener(message: Message)
    }

    interface FirebaseChangeListener {
        fun getMessageChange(message: Message)
    }
}