package sms.newgate.com.smseditorremote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import android.os.CountDownTimer
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    lateinit var adapter: MsgAdapter

    lateinit var arrayMessage: ArrayList<Message>

    lateinit var arraySimMessage: ArrayList<Message>

    var address = ""

    var beforeMessage: Message = Message()

    var afterMessage: Message = Message()

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

        adapter = MsgAdapter(arrayMessage, object: MsgAdapter.ClickMsgItemListener {
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
            if(arraySimMessage[i].address == address) {
                arrayMessage.add(arraySimMessage[i])
            }
        }
        return arrayMessage
    }

    fun deleteMessage(messageId: String) {
        for(i in  arrayMessage.indices) {
            if(arrayMessage[i].id == messageId) {
                arrayMessage.removeAt(i)
                break
            }
        }
    }

    fun checkMessage(message: Message): Boolean {
        val arrayIdMessage = arrayListOf<String>()
        for(i in  arrayMessage.indices) {
            arrayIdMessage.add(arrayMessage[i].id)
        }
        return arrayIdMessage.contains(message.id)
    }

    fun openDialogEditor(pos: Int) {
        beforeMessage.address = arrayMessage[pos].address
        beforeMessage.body = arrayMessage[pos].body
        var messageEdit = arrayMessage[pos]
        val fm = fragmentManager
        val editMsgDialogFragment = EditMessageDialogFragment.newInstance(messageEdit)
        editMsgDialogFragment.show(fm, "")
        editMsgDialogFragment.setEditMessageListener(object: EditMessageDialogFragment.EditMessageListener {
            override fun editMessage(message: Message) {
                firebaseInstance.updateMessage(message)
                createCountDown()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        FirebaseUtils.getInstance(this).getMessageChange(object: FirebaseUtils.FirebaseChangeListener {
            override fun getMessageChange(message: Message) {
                for(i in arrayMessage.indices) {
                    if(arrayMessage[i].id == message.id && arrayMessage[i].simSerialNumber == message.simSerialNumber) {
                        afterMessage = message
                        deleteMessage(message.id)
                        arrayMessage.add(message)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    fun createCountDown() {
        object : CountDownTimer(3000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                if(beforeMessage.address == afterMessage.address && beforeMessage.body == afterMessage.body) {
                    showMessageToast("Sửa message lỗi !")
                } else {
                    showMessageToast("Sửa message thành công !")
                }
            }
        }.start()
    }

    fun showMessageToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
