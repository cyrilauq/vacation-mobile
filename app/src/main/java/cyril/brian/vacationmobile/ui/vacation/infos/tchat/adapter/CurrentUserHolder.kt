package cyril.brian.vacationmobile.ui.vacation.infos.tchat.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.vacation.infos.tchat.MessageInfos

class CurrentUserHolder(view: View) : RecyclerView.ViewHolder(view) {
    val messageText = view.findViewById<TextView>(R.id.text_gchat_message_me)
    val messageDate = view.findViewById<TextView>(R.id.text_gchat_timestamp_me)

    fun bindView(context: Context, message: MessageInfos) {
        messageText.setText(message.content)
        messageDate.text = message.dateTimeSent
    }
}