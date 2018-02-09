package sms.newgate.com.smseditorremote.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_address.*
import sms.newgate.com.smseditorremote.*
import sms.newgate.com.smseditorremote.adapter.AddressAdapter
import sms.newgate.com.smseditorremote.model.Message
import sms.newgate.com.smseditorremote.utils.FirebaseUtils
import sms.newgate.com.smseditorremote.utils.PrefsUtil
import sms.newgate.com.smseditorremote.utils.replaceAddress
import java.util.*

/**
 * Created by apple on 2/3/18.
 */
class AddressActivity : AppCompatActivity() {

    lateinit var adapter: AddressAdapter

    lateinit var arrayAddress: ArrayList<Message>

    lateinit var arraySimMessage: ArrayList<Message>

    lateinit var arrayAllMessage: ArrayList<Message>

    lateinit var simNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        arraySimMessage = arrayListOf()

        arrayAddress = arrayListOf()

        simNumber = intent.extras.getString("sim_number")

        if (PrefsUtil.getInstance(this).checkPrefExits(simNumber)) {
            title = PrefsUtil.getInstance(this).getPref(simNumber)
        } else {
            title = simNumber
        }

        arrayAllMessage = intent.extras.getParcelableArrayList("allmessage")


        arraySimMessage.addAll(getSimMessage())

        loadAddress()

        adapter = AddressAdapter(arrayAddress, object : AddressAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
                openMainActivity(arrayAddress[pos].address)
            }

        })
        addressRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        addressRecyclerView.adapter = adapter
    }


    fun loadAddress() {
        arraySimMessage.reverse()
        for(i in arraySimMessage.indices) {
            if(!checkMessageAddress(arraySimMessage[i])) {
                arrayAddress.add(arraySimMessage[i])
            }
        }
        Collections.sort(arraySimMessage)
    }

    fun checkMessageAddress(message: Message): Boolean {
        var isCheck : Boolean = false
        for(i in  arrayAddress.indices) {
            if(arrayAddress[i].address == message.address || arrayAddress[i].address.replaceAddress() == message.address
            || arrayAddress[i].address == message.address.replaceAddress()) {
                isCheck = true
                break
            }
        }
        return isCheck
    }

    fun getSimMessage() : ArrayList<Message> {
        var arraySimMessage: ArrayList<Message> = arrayListOf()
        for(i in arrayAllMessage.indices) {
            if(arrayAllMessage[i].simSerialNumber == simNumber) {
                 arraySimMessage.add(arrayAllMessage[i])
            }
        }
        return arraySimMessage
    }

    fun openMainActivity(address: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("address", address)
        intent.putExtra("simmessage", arraySimMessage)
        startActivity(intent)
    }

    fun deleteMessage(simId: String) {
        for(i in  arraySimMessage.indices) {
            if(arraySimMessage[i].simId == simId) {
                arraySimMessage.removeAt(i)
                break
            }
        }
    }

    override fun onResume() {
        super.onResume()
        FirebaseUtils.getInstance(this).getMessageChange(object: FirebaseUtils.FirebaseChangeListener {
            override fun getMessageChange(message: Message) {
                if(message.simSerialNumber != simNumber)
                    return
                for(i in arraySimMessage.indices) {
                    if(arraySimMessage[i].simId == message.simId) {
                        deleteMessage(message.simId)
                        arraySimMessage.add(i, message)
                    }
                }
            }

        })
    }
}