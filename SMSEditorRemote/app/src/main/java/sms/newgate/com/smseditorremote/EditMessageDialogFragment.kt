package sms.newgate.com.smseditorremote

import android.app.DialogFragment
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.layout_edit_msg.*
/**
 * Created by apple on 1/28/18.
 */
class EditMessageDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(message: Message): EditMessageDialogFragment {
            val frag = EditMessageDialogFragment()
            val args = Bundle()
            args.putParcelable("smsthread", message)
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
        val message = arguments.getParcelable<Message>("smsthread")
        addressEditText.setText(message.address)
        dateEditText.setText(message.date)
        bodyEditText.setText(message.body)
        editButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                message.address = addressEditText.text.toString()
                message.body = bodyEditText.text.toString()
                message.date = dateEditText.text.toString()
                listener?.editMessage(message)
                dialog.hide()
            }
        })
    }

    interface EditMessageListener {
        fun editMessage(message: Message)
    }
}