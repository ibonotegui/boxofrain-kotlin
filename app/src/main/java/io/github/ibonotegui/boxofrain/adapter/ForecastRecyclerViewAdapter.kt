package io.github.ibonotegui.boxofrain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.ibonotegui.boxofrain.R
import io.github.ibonotegui.boxofrain.databinding.DailyListItemLayoutBinding
import io.github.ibonotegui.boxofrain.model.DataPoint
import io.github.ibonotegui.boxofrain.util.BoxFormat

class ForecastRecyclerViewAdapter(
    private val context: Context,
    private var dataPoints: List<DataPoint>,
    private val timezone: String
) : RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder>() {

    private var data = emptyList<DataPoint>()

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

    internal fun setData(dataPoints: List<DataPoint>) {
        this.dataPoints = dataPoints
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: DailyListItemLayoutBinding = DailyListItemLayoutBinding.bind(view)
        val dateTextView: TextView = binding.dailyDateTextView
        val summaryTextView: TextView = binding.dailySummaryTextView
        val minTemperatureTextView: TextView = binding.dailyMinTemperatureTextView
        val maxTemperatureTextView: TextView = binding.dailyMaxTemperatureTextView
        val windSpeedTextView: TextView = binding.dailyWindSpeedTextView
        val dailyPrecipitationProbTextView: TextView = binding.dailyPrecipitationProbTextView
    }

}
