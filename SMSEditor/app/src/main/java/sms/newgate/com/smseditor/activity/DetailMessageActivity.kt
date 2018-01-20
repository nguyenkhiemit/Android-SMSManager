package sms.newgate.com.smseditor.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_detail.*
import sms.newgate.com.smseditor.R
import sms.newgate.com.smseditor.adapter.MsgDetailAdapter
import sms.newgate.com.smseditor.model.SmsThread
import sms.newgate.com.smseditor.util.FirebaseUtils
import sms.newgate.com.smseditor.util.MessageHelper
import sms.newgate.com.smseditor.view.EditMessageDialogFragment

/**
 * Created by apple on 1/19/18.
 */
class DetailMessageActivity: AppCompatActivity() {

    lateinit var helper: MessageHelper

    lateinit var adapter: MsgDetailAdapter

    lateinit var smsThreads: ArrayList<SmsThread>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val threadId = intent.getStringExtra("thread_id")

        helper = MessageHelper(this)

        smsThreads = helper.getThreadMessage(threadId)

        title = smsThreads[0].address

        FirebaseUtils.getInstance(this).createMessages(smsThreads)

        adapter = MsgDetailAdapter(smsThreads, object: MsgDetailAdapter.ClickMsgItemListener {
            override fun click(pos: Int) {
                openDialogEditor(pos)
            }

        })

        msgDetailRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        msgDetailRecyclerView.adapter = adapter
    }

    fun openDialogEditor(pos: Int) {
        val fm = fragmentManager
        val editMsgDialogFragment = EditMessageDialogFragment.newInstance(smsThreads[pos])
        editMsgDialogFragment.show(fm, "")
        editMsgDialogFragment.setEditMessageListener(object: EditMessageDialogFragment.EditMessageListener {
            override fun reloadMessage() {
            }

            override fun editMessage(newSmsThread: SmsThread) {
                helper.updateMessage(newSmsThread)
//                helper.storeMessage()
            }

        })
    }
}