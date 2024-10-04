package cyril.brian.vacationmobile.ui.vacation.infos.tchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.vacation.infos.tchat.MessageInfos

class MessageAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_USER_MESSAGE_ME = 10
    private val VIEW_TYPE_USER_MESSAGE_OTHER = 11

    private var messages: MutableList<MessageInfos>
    private var context: Context

    init {
        messages = ArrayList()
        this.context = context
    }

    fun loadMessages(messages: MutableList<MessageInfos>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    fun addFirst(message: MessageInfos) {
        messages.add(0, message)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            VIEW_TYPE_USER_MESSAGE_ME  -> {
                CurrentUserHolder(layoutInflater.inflate(R.layout.item_chat_me, parent, false))
            }
            VIEW_TYPE_USER_MESSAGE_OTHER ->  {
                OtherUserHolder(layoutInflater.inflate(R.layout.item_chat_other, parent, false))
            }
            else -> CurrentUserHolder(layoutInflater.inflate(R.layout.item_chat_me, parent, false)) //Generic return

        }
    }

    override fun getItemViewType(position: Int): Int = if (messages[position].isOther) VIEW_TYPE_USER_MESSAGE_OTHER else VIEW_TYPE_USER_MESSAGE_ME

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            VIEW_TYPE_USER_MESSAGE_ME -> {
                holder as CurrentUserHolder
                holder.bindView(context, messages[position])
            }
            VIEW_TYPE_USER_MESSAGE_OTHER -> {
                holder as OtherUserHolder
                holder.bindView(context, messages[position])
            }
        }
    }
}