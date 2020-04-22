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
    private val dataPoints: List<DataPoint>,
    private val timezone: String
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
        val dataPoint = dataPoints[position]
        holder.dateTextView.text = BoxFormat.formatDateDayLong(dataPoint.time, timezone)
        holder.summaryTextView.text = dataPoint.summary
        holder.minTemperatureTextView.text =
            BoxFormat.formatTemperature(dataPoint.temperatureMin)
        holder.maxTemperatureTextView.text =
            BoxFormat.formatTemperature(dataPoint.temperatureMax)
        holder.windSpeedTextView.text = String.format(
            context.getString(R.string.wind_speed),
            dataPoint.windSpeed,
            BoxFormat.getWindBearing(dataPoint.windBearing)
        )
        holder.dailyPrecipitationProbTextView.text = String.format(
            context.getString(R.string.rain),
            dataPoint.precipProbability * 100
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.daily_date_text_view
        val summaryTextView: TextView = view.daily_summary_text_view
        val minTemperatureTextView: TextView = view.daily_min_temperature_text_view
        val maxTemperatureTextView: TextView = view.daily_max_temperature_text_view
        val windSpeedTextView: TextView = view.daily_wind_speed_text_view
        val dailyPrecipitationProbTextView: TextView = view.daily_precipitation_prob_text_view
    }

}
