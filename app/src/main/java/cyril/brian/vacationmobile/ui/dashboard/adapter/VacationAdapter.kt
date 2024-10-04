package cyril.brian.vacationmobile.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.infrastructure.responses.GetVacationResultItemResponse
import cyril.brian.vacationmobile.ui.RecyclerViewEvent

class VacationAdapter(
    private val vacationList: List<GetVacationResultItemResponse>,
    private val layoutId: Int,
    private val listener: RecyclerViewEvent) : RecyclerView.Adapter<VacationAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val vacationName: TextView = view.findViewById(R.id.vacation_title)
        val vacationPlace: TextView = view.findViewById(R.id.vacation_place)
        val vacationDate: TextView = view.findViewById(R.id.vacation_date)

        // Sert à placer un click listener sur un item précis. Si on veux que
        // le click ne soit détecter que sur le titre (par exemple), à la place de
        // view.setOnClickListener on mettrait vacationName.setOnClickListener
        init {
            view.setOnClickListener(this)
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
            .inflate(layoutId, parent, false)



        return ViewHolder(view)
    }

    override fun getItemCount(): Int = vacationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentVacation = vacationList[position]

        holder.vacationName.text = currentVacation.title
        // This substring shouldn't be there but we don't have enough time to correct it - Cyril
        holder.vacationDate.text = "%s - %s".format(currentVacation.dateTimeBegin.substring(0, currentVacation.dateTimeBegin.indexOf("T")), currentVacation.dateTimeEnd.substring(0, currentVacation.dateTimeBegin.indexOf("T")))
        holder.vacationPlace.text = currentVacation.place
    }
}