package sms.newgate.com.smseditorremote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: MsgAdapter

    lateinit var arrayMessage: ArrayList<Message>

    val firebaseInstance by lazy {
        FirebaseUtils.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayMessage = arrayListOf()
        firebaseInstance.getAllMessage(object: FirebaseUtils.FirebaseListener {
            override fun getAllMessageListener(message: Message) {
                if(!checkMessage(message)) {
                    arrayMessage.add(message)
                } else {
                    deleteMessage(message.id)
                    arrayMessage.add(message)
                }
                adapter.notifyDataSetChanged()
            }

        })

        adapter = MsgAdapter(arrayMessage, object: MsgAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
                openDialogEditor(pos)
            }

        })
        msgThreadRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        msgThreadRecyclerView.adapter = adapter
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
        val fm = fragmentManager
        val editMsgDialogFragment = EditMessageDialogFragment.newInstance(arrayMessage[pos])
        editMsgDialogFragment.show(fm, "")
        editMsgDialogFragment.setEditMessageListener(object: EditMessageDialogFragment.EditMessageListener {

            override fun editMessage(message: Message) {
                firebaseInstance.updateMessage(message)
            }

        })
    }
}
