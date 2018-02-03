package sms.newgate.com.smseditorremote

import android.content.Context
import android.support.v7.widget.RecyclerView
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
        fun bindData(message: Message, pos: Int) {
            itemView.simTextView.text = message.simSerialNumber
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View?) {
                    listener.click(pos)
                }

            })
        }
    }

    interface ClickMsgItemListener {
        fun click(pos: Int)
    }
}