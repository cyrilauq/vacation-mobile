package cyril.brian.vacationmobile.ui.vacation.infos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.domain.User
import cyril.brian.vacationmobile.ui.vacation.infos.VacationFragment

class MemberAdapter(
    private val context: VacationFragment,
    private val membersList: List<User>,
    private val layoutId: Int
) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val memberFullname: TextView = view.findViewById(R.id.member_fullname)
        val memberMail: TextView = view.findViewById(R.id.member_mail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = membersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentMember = membersList[position]

        holder.memberFullname.text = currentMember.fullName
        holder.memberMail.text = currentMember.mail
    }
}