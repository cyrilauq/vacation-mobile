package cyril.brian.vacationmobile.ui.vacation.infos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cyril.brian.vacationmobile.R
import cyril.brian.vacationmobile.domain.Forecast
import cyril.brian.vacationmobile.ui.vacation.infos.VacationFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherAdapter(private val context: VacationFragment,
                     private val weatherList: List<Forecast>,
                     private val layoutId: Int
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, packName: String) : RecyclerView.ViewHolder(view) {
        val weatherDate: TextView = view.findViewById(R.id.weather_date)
        val weatherDescription: TextView = view.findViewById(R.id.weather_description)
        val weatherImage: ImageView = view.findViewById(R.id.weather_image)
        val packageName: String = packName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)


        return ViewHolder(view, parent.context.packageName)
    }

    override fun getItemCount(): Int = weatherList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = weatherList[position]

        holder.weatherDescription.text = "${currentWeather.temp}°C - ${currentWeather.weather} | ${currentWeather.description}"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.FRANCE)
        val netDate = Date(currentWeather.timestamp.toLong() * 1000)
        holder.weatherDate.text = dateFormat.format(netDate)

        val resId: Int = context.resources.getIdentifier("ic_" + currentWeather.icon, "drawable", holder.packageName)
        holder.weatherImage.setImageDrawable(context.resources.getDrawable(resId))
    }
}