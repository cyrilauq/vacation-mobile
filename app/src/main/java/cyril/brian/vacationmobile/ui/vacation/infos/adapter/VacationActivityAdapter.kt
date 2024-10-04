package cyril.brian.vacationmobile.ui.vacation.infos.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.ui.vacation.infos.VacationFragment

class VacationActivityAdapter(
    private val context: VacationFragment,
    private val activitiesList: List<ActivityModel>,
    private val layoutId: Int,
    private val consumer: ((String) -> Unit)? = null
) : RecyclerView.Adapter<VacationActivityAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var activityId:String? = null
        val activityName: TextView = view.findViewById(R.id.activity_title)
        val activityPlace: TextView = view.findViewById(R.id.activity_place)
        val activityDescription: TextView = view.findViewById(R.id.activity_description)
        val openMapBtn: ImageButton = view.findViewById(R.id.open_map_btn)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                consumer?.invoke(activityId!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = activitiesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentActivity = activitiesList[position]

        holder.activityId = currentActivity.activityId
        holder.activityName.text = currentActivity.title
        holder.activityDescription.text = currentActivity.description
        holder.activityPlace.text = currentActivity.place
        holder.openMapBtn.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=${currentActivity.latitude},${currentActivity.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(context.requireActivity().packageManager)?.let {
                context.startActivity(mapIntent)
            }
        }
    }

}