package io.github.ibonotegui.boxofrain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.ibonotegui.boxofrain.R
import io.github.ibonotegui.boxofrain.model.DataPoint
import io.github.ibonotegui.boxofrain.util.BoxFormat
import kotlinx.android.synthetic.main.daily_list_item_layout.view.*

class ForecastRecyclerViewAdapter(
    private val context: Context,
    private val dataPoints: List<DataPoint>
) :
    RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.daily_list_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataPoints.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dateTextView.text = BoxFormat.formatDateDayLong(dataPoints[position].time)
        holder.summaryTextView.text = dataPoints[position].summary
        holder.minTemperatureTextView.text =
            BoxFormat.formatTemperature(dataPoints[position].temperatureMin)
        holder.maxTemperatureTextView.text =
            BoxFormat.formatTemperature(dataPoints[position].temperatureMax)
        //holder.windSpeedTextView.text = BoxFormat.getWindBearing(dataPoints[position].windBearing)
        holder.dailyPrecipitationProbTextView.text = String.format(
            context.getString(R.string.rain),
            dataPoints[position].precipProbability * 100
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.daily_date_text_view
        val summaryTextView: TextView = view.daily_summary_text_view
        val minTemperatureTextView: TextView = view.daily_min_temperature_text_view
        val maxTemperatureTextView: TextView = view.daily_max_temperature_text_view
        //val windSpeedTextView: TextView = view.daily_wind_speed_textview
        val dailyPrecipitationProbTextView: TextView = view.daily_precipitation_prob_text_view
    }

}
