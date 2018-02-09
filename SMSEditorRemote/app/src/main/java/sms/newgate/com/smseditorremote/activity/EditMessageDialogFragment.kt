package sms.newgate.com.smseditorremote.activity

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_edit_msg.*
import sms.newgate.com.smseditorremote.model.Message
import sms.newgate.com.smseditorremote.R
import sms.newgate.com.smseditorremote.utils.*

/**
 * Created by apple on 1/28/18.
 */
class EditMessageDialogFragment : DialogFragment() {

    val utils: Utils by lazy {
        Utils()
    }

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

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        val message = arguments.getParcelable<Message>("smsthread")
        val date = message.date
        addressEditText.setText(message.address)
        bodyEditText.setText(message.body)
        if(date != null) {
            yearText.setText("" + utils.getYearFromDate(date))
            monthText.setText("" + utils.getMonthFromDate(date))
            dayText.setText("" + utils.getDayFromDate(date))
            hourText.setText("" + utils.getHourFromDate(date))
            minuteText.setText("" + utils.getMinuteFromDate(date))
            secondText.setText("" + utils.getSecondFromDate(date))
        }
        editButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val messageErr = validateTime(yearText.text.toString().toInt(), monthText.text.toString().toInt(), dayText.text.toString().toInt(),
                        hourText.text.toString().toInt(), minuteText.text.toString().toInt(), secondText.text.toString().toInt())
                if(messageErr != "") {
                    Toast.makeText(activity, messageErr, Toast.LENGTH_SHORT).show()
                    return
                }
                message.address = addressEditText.text.toString()
                message.body = bodyEditText.text.toString()
                message.date = fixLengthDate(yearText.text.toString()) + "-" + fixLengthDate(monthText.text.toString()) + "-" +
                        fixLengthDate(dayText.text.toString()) + " " +
                        fixLengthDate(hourText.text.toString()) + ":" + fixLengthDate(minuteText.text.toString()) + ":" +
                        fixLengthDate(secondText.text.toString())
                listener?.editMessage(message)
                dialog.dismiss()
            }
        })
    }

    interface EditMessageListener {
        fun editMessage(message: Message)
    }

    fun fixLengthDate(time: String) : String {
        if(time.length == 1) {
            return "0" + time
        }
        return time
    }

    fun validateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): String {
        var message = ""
        if(year < 2007) {
            message = "Năm phải lớn hơn 2007"
        } else if(year > 2028) {
            message = "Năm phải lớn hơn 2028"
        } else if(month > 12) {
            message = "Tháng nhỏ hơn 12"
        } else if(day > 31) {
            message = "Ngày nhỏ hơn 31"
        } else if(hour >= 24) {
            message = "Giờ nhỏ hơn 24"
        } else if(minute >= 60) {
            message = "Phút nhỏ hơn 60"
        } else if(second > 60) {
            message = "Giây nhỏ hơn 60"
        }
        return message
    }
}