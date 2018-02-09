package sms.newgate.com.smseditorremote.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import sms.newgate.com.smseditorremote.*
import sms.newgate.com.smseditorremote.adapter.MsgAdapter
import sms.newgate.com.smseditorremote.model.Message
import sms.newgate.com.smseditorremote.utils.FirebaseUtils
import sms.newgate.com.smseditorremote.utils.replaceAddress
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: MsgAdapter

    lateinit var arrayMessage: ArrayList<Message>

    lateinit var arraySimMessage: ArrayList<Message>

    var address = ""

    val firebaseInstance by lazy {
        FirebaseUtils.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayMessage = arrayListOf()

        address = intent.extras.getString("address")

        title = address

        arraySimMessage = intent.extras.getParcelableArrayList("simmessage")

        arrayMessage.addAll(checkMessageAddress())

        Collections.sort(arrayMessage)

        adapter = MsgAdapter(arrayMessage, object : MsgAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
                openDialogEditor(pos)
            }

        })
        msgThreadRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        msgThreadRecyclerView.adapter = adapter
    }

    fun checkMessageAddress() : ArrayList<Message> {
        var arrayMessage: ArrayList<Message> = arrayListOf()
        for(i in  arraySimMessage.indices) {
            if(arraySimMessage[i].address == address || arraySimMessage[i].address.replaceAddress() == address ||
                    arraySimMessage[i].address == address.replaceAddress()) {
                arrayMessage.add(arraySimMessage[i])
            }
        }
        return arrayMessage
    }

    fun deleteMessage(simId: String) {
        for(i in  arrayMessage.indices) {
            if(arrayMessage[i].simId == simId) {
                arrayMessage.removeAt(i)
                break
            }
        }
    }

    fun openDialogEditor(pos: Int) {
        var messageEdit = arrayMessage[pos]
        val fm = fragmentManager
        val editMsgDialogFragment = EditMessageDialogFragment.newInstance(messageEdit)
        editMsgDialogFragment.show(fm, "")
        editMsgDialogFragment.setEditMessageListener(object: EditMessageDialogFragment.EditMessageListener {
            override fun editMessage(message: Message) {
                firebaseInstance.updateMessage(message)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        FirebaseUtils.getInstance(this).getMessageChange(object: FirebaseUtils.FirebaseChangeListener {
            override fun getMessageChange(message: Message) {
                for(i in arrayMessage.indices) {
                    if(arrayMessage[i].simId == message.simId) {
                        deleteMessage(message.simId)
                        arrayMessage.add(i, message)
                        adapter.notifyDataSetChanged()
                    }
                }
                Log.e("XStatusMessage","=====>" + message.status)
                if(message.status == 1) {
                    showMessageToast("Update message thành công !!!")
                    message.status = 0
                    firebaseInstance.updateMessage(message)
                } else if(message.status == 2) {
                    showMessageToast("Update message thất bại !!!")
                    message.status = 0
                    firebaseInstance.updateMessage(message)
                }
            }

        })
    }

    fun showMessageToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
