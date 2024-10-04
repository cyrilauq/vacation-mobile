package cyril.brian.vacationmobile.ui.vacation.infos.tchat.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.vacation.infos.tchat.MessageInfos

class OtherUserHolder(view: View) : RecyclerView.ViewHolder(view) {
    val messageText = view.findViewById<TextView>(R.id.text_gchat_message_other)
    val messageDate = view.findViewById<TextView>(R.id.text_gchat_timestamp_other)
    val profileImage  = view.findViewById<ImageView>(R.id.image_gchat_profile_other)
    val user = view.findViewById<TextView>(R.id.text_gchat_user_other)

    fun bindView(context: Context, message: MessageInfos) {
        messageText.setText(message.content)
        messageDate.text = message.dateTimeSent
        user.text = message.senderUserName
    }
}