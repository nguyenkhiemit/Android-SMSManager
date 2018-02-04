package sms.newgate.com.smseditorremote

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_item_layout.view.*
import kotlinx.android.synthetic.main.sim_item_layout.view.*


/**
 * Created by apple on 1/17/18.
 */
class SimAdapter(val smsThreads: ArrayList<Message>, val listener: ClickMsgItemListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sim_item_layout, parent, false)
        return MsgViewHolder(parent.context, v, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is MsgViewHolder) {
            holder.bindData(smsThreads.get(position), position)
        }
    }

    override fun getItemCount(): Int {
        return smsThreads.size
    }

    class MsgViewHolder(val context: Context, itemView: View, val listener: ClickMsgItemListener): RecyclerView.ViewHolder(itemView) {

        var simNumber: String = ""

        fun bindData(message: Message, pos: Int) {
            simNumber = message.simSerialNumber
            if(PrefsUtil.getInstance(context).checkPrefExits(message.simSerialNumber)) {
                itemView.simTextView.text = PrefsUtil.getInstance(context).getPref(message.simSerialNumber)
            } else {
                itemView.simTextView.text = message.simSerialNumber
            }
            itemView.simEditText.setText(itemView.simTextView.text.toString())
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View?) {
                    listener.click(pos)
                }

            })
            itemView.changeButton.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    if(itemView.changeButton.text == "OK") {
                        itemView.simTextView.visibility = View.VISIBLE
                        itemView.simEditText.visibility = View.INVISIBLE
                        itemView.changeButton.text = "Sửa"
                        if(simNumber != itemView.simEditText.text.toString()) {
                            itemView.simTextView.text = itemView.simEditText.text
                            PrefsUtil.getInstance(context).savePref(simNumber, itemView.simEditText.text.toString())
                        }
                    } else {
                        itemView.simTextView.visibility = View.INVISIBLE
                        itemView.simEditText.visibility = View.VISIBLE
                        itemView.changeButton.text = "OK"
                    }
                }

            })
        }
    }

    interface ClickMsgItemListener {
        fun click(pos: Int)
    }
}