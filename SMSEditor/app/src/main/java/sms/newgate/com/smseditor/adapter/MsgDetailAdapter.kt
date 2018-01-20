package sms.newgate.com.smseditor.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.msg_thread_item_detail_layout.view.*
import sms.newgate.com.smseditor.R
import sms.newgate.com.smseditor.activity.DetailMessageActivity
import sms.newgate.com.smseditor.model.SmsThread
import sms.newgate.com.smseditor.util.CircleTransform

/**
 * Created by apple on 1/17/18.
 */
class MsgDetailAdapter(val smsThreads: ArrayList<SmsThread>, val listener: ClickMsgItemListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.msg_thread_item_detail_layout, parent, false)
        return MsgViewHolder(parent.context, v, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is MsgViewHolder) {
            holder.bindData(smsThreads[position], position)
        }
    }

    override fun getItemCount(): Int {
        return smsThreads.size
    }

    class MsgViewHolder(val context: Context, itemView: View, val listener: ClickMsgItemListener): RecyclerView.ViewHolder(itemView) {
        fun bindData(msmThread: SmsThread, pos: Int) {
            Picasso.with(context).load(R.drawable.ic_avatar).placeholder(R.drawable.ic_avatar).transform(CircleTransform()).into(itemView.avatarDetailImage)
            itemView.bodyDetailText.text = msmThread.body
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