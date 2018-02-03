package sms.newgate.com.smseditorremote

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_item_layout.view.*
import kotlinx.android.synthetic.main.message_item_layout_friend.view.*


/**
 * Created by apple on 1/17/18.
 */
class MsgAdapter(val smsThreads: ArrayList<Message>, val listener: ClickMsgItemListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_MY = 1
    private val TYPE_FRIEND = 2

    override fun getItemViewType(position: Int): Int {
        if(smsThreads[position].type.equals("" + TYPE_MY)) {
            return TYPE_MY
        } else {
            return TYPE_FRIEND
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        var viewHolder: RecyclerView.ViewHolder? = null
        if(viewType == TYPE_MY) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_layout, parent, false)
            viewHolder = MsgViewHolder(parent.context, view, listener)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.message_item_layout_friend, parent, false)
            viewHolder = MsgFriendViewHolder(parent.context, view, listener)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val message = smsThreads[position]
        if(message.type.equals("" + TYPE_MY)) {
            if(holder is MsgViewHolder) {
                holder.bindData(message, position)
            }
        } else {
            if(holder is MsgFriendViewHolder) {
                holder.bindData(message, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return smsThreads.size
    }


    class MsgFriendViewHolder(val context: Context, itemView: View, val listener: ClickMsgItemListener): RecyclerView.ViewHolder(itemView) {
        fun bindData(msmThread: Message, pos: Int) {
            Picasso.with(context).load(R.drawable.ic_friend).placeholder(R.drawable.ic_friend).transform(CircleTransform()).into(itemView.avatarImageFriend)
            itemView.addressTextFriend.text = msmThread.address
            itemView.bodyTextFriend.text = msmThread.body
            itemView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View?) {
                    listener.click(pos)
                }

            })
        }
    }

    class MsgViewHolder(val context: Context, itemView: View, val listener: ClickMsgItemListener): RecyclerView.ViewHolder(itemView) {
        fun bindData(msmThread: Message, pos: Int) {
            Picasso.with(context).load(R.drawable.ic_avatar).placeholder(R.drawable.ic_avatar).transform(CircleTransform()).into(itemView.avatarImage)
            itemView.addressText.text = msmThread.address
            itemView.bodyText.text = msmThread.body
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