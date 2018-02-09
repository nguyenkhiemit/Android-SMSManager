package sms.newgate.com.smseditor.view

import android.app.DialogFragment
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.layout_edit_msg.*
import sms.newgate.com.smseditor.R
import sms.newgate.com.smseditor.model.SmsThread

/**
 * Created by apple on 1/17/18.
 */
class EditMessageDialogFragment : DialogFragment() {

    internal var uri = Uri.parse("content://sms")

    var smsMessageId: String = ""

    companion object {
        fun newInstance(smsThread: SmsThread): EditMessageDialogFragment {
            val frag = EditMessageDialogFragment()
            val args = Bundle()
            args.putParcelable("smsthread", smsThread)
            frag.arguments = args
            return frag
        }
    }

    var listener: EditMessageListener? = null

    fun setEditMessageListener(listenser: EditMessageListener) {
        this.listener = listenser
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.layout_edit_msg, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.window.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        val smsThread = arguments.getParcelable<SmsThread>("smsthread")
        smsMessageId = smsThread.id
        val smsMessageType = smsThread.type
        val smsMessageAddress = smsThread.address
        addressEditText.setText(smsThread.address)
        dateEditText.setText(smsThread.date)
        bodyEditText.setText(smsThread.body)
        editButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val smsThread = SmsThread()
                smsThread.id = smsMessageId
                smsThread.type = smsMessageType
                smsThread.address = addressEditText.text.toString()
                smsThread.body = bodyEditText.text.toString()
                smsThread.date = dateEditText.text.toString()
                listener?.editMessage(smsThread)
                dialog.hide()
            }
        })
    }

    interface EditMessageListener {
        fun editMessage(newSmsThread: SmsThread)
        fun reloadMessage()
    }
}