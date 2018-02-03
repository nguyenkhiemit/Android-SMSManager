package sms.newgate.com.smseditorremote

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_home.*

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

        title = simNumber

        arrayAllMessage = intent.extras.getParcelableArrayList("allmessage")

        arraySimMessage.addAll(getSimMessage())

        loadAddress()

        adapter = AddressAdapter(arrayAddress, object: AddressAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
                openMainActivity(arrayAddress[pos].address)
            }

        })
        addressRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        addressRecyclerView.adapter = adapter
    }


    fun loadAddress() {
        for(i in arraySimMessage.indices) {
            if(!checkMessageAddress(arraySimMessage[i])) {
                arrayAddress.add(arraySimMessage[i])
            }
        }
    }

    fun checkMessageAddress(message: Message): Boolean {
        var isCheck : Boolean = false
        for(i in  arrayAddress.indices) {
            if(arrayAddress[i].address == message.address) {
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
}