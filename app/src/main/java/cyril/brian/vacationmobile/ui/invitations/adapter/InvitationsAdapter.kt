package cyril.brian.vacationmobile.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.RecyclerViewEvent
import cyril.brian.vacationmobile.ui.invitations.adapter.InvitationModel

class InvitationsAdapter(
    private val invitationList: List<InvitationModel>,
    private val listener: RecyclerViewEvent) : RecyclerView.Adapter<InvitationsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val invitation_name: TextView = view.findViewById(R.id.invitation_name)
        val invitation_title: TextView = view.findViewById(R.id.invitation_title)
        val invitation_accept_btn: Button = view.findViewById(R.id.invitation_accept_btn)

        // Sert à placer un click listener sur un item précis. Si on veux que
        // le click ne soit détecter que sur le titre (par exemple), à la place de
        // view.setOnClickListener on mettrait vacationName.setOnClickListener
        init {
            invitation_accept_btn.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_invitation, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = invitationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = invitationList[position]
        holder.invitation_title.text = current.vacationName
        holder.invitation_name.text = "Invitation for: ${current.vacationName}"
        if(current.isAccepted) {
            holder.invitation_accept_btn.visibility = View.GONE
        }
    }
}