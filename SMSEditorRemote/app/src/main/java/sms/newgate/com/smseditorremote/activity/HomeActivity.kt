package sms.newgate.com.smseditorremote.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_home.*
import sms.newgate.com.smseditorremote.utils.FirebaseUtils
import sms.newgate.com.smseditorremote.model.Message
import sms.newgate.com.smseditorremote.R
import sms.newgate.com.smseditorremote.adapter.SimAdapter

/**
 * Created by apple on 2/3/18.
 */
class HomeActivity : AppCompatActivity() {

    lateinit var adapter: SimAdapter

    lateinit var arraySimNumber: ArrayList<Message>

    lateinit var arrayAllMessage: ArrayList<Message>

    val firebaseInstance by lazy {
        FirebaseUtils.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        arraySimNumber = arrayListOf()
        arrayAllMessage = arrayListOf()
        firebaseInstance.getAllMessage(object: FirebaseUtils.FirebaseListener {
            override fun getAllMessageListener(message: Message) {
                if(!checkMessageId(message)) {
                    Log.e("XgetAllMessage", "====> add")
                    arrayAllMessage.add(message)
                } else {
                    Log.e("XgetAllMessage", "====> update")
                    deleteMessage(message)
                    arrayAllMessage.add(message)
                }

                if(!checkMessage(message)) {
                    arraySimNumber.add(message)
                }
                adapter.notifyDataSetChanged()
            }

        })

        adapter = SimAdapter(arraySimNumber, object : SimAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
                openAddressActivity(arraySimNumber[pos].simSerialNumber)
            }

        })
        phoneRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        phoneRecyclerView.adapter = adapter
    }

    fun checkMessageId(message: Message): Boolean {
        val arraySimIdMessage = arrayListOf<String>()
        for(i in  arrayAllMessage.indices) {
            arraySimIdMessage.add(arrayAllMessage[i].simId)
        }
        return  arraySimIdMessage.contains(message.simId)
    }

    fun checkMessage(message: Message): Boolean {
        val arraySimPhone = arrayListOf<String>()
        for(i in  arraySimNumber.indices) {
            arraySimPhone.add(arraySimNumber[i].simSerialNumber)
        }
        return arraySimPhone.contains(message.simSerialNumber)
    }

    fun deleteMessage(message: Message) {
        for(i in  arrayAllMessage.indices) {
            if(arrayAllMessage[i].simId == message.simId) {
                arrayAllMessage.removeAt(i)
                break
            }
        }
    }

    fun openAddressActivity(simNumber: String) {
        val intent = Intent(this, AddressActivity::class.java)
        intent.putExtra("sim_number", simNumber)
        intent.putExtra("allmessage", arrayAllMessage)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        FirebaseUtils.getInstance(this).getMessageChange(object: FirebaseUtils.FirebaseChangeListener {
            override fun getMessageChange(message: Message) {
                for(i in arrayAllMessage.indices) {
                    if(arrayAllMessage[i].simId == message.simId) {
                        deleteMessage(message)
                        arrayAllMessage.add(i, message)
                    }
                }
            }

        })
    }
}