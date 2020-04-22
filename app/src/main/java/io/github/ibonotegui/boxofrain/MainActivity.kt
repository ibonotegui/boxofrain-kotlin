package io.github.ibonotegui.boxofrain

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.ibonotegui.boxofrain.adapter.ForecastRecyclerViewAdapter
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.model.Location
import io.github.ibonotegui.boxofrain.network.Status
import io.github.ibonotegui.boxofrain.util.BoxConstants
import io.github.ibonotegui.boxofrain.util.BoxFormat
import io.github.ibonotegui.boxofrain.util.BoxPreferences
import io.github.ibonotegui.boxofrain.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = BoxPreferences.getLocation(applicationContext)

        if (location == null) {

            val searchIntent = Intent(this, SearchLocationActivity::class.java)
            startActivity(searchIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()

        } else {

            setContentView(R.layout.activity_main)
            title = location.name

            daily_recycler_view.layoutManager = LinearLayoutManager(this)
            daily_recycler_view.setHasFixedSize(true)

            getForecast(location)

            swipe_refresh_layout.setColorSchemeColors(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.blue
                )
            )
            swipe_refresh_layout.setOnRefreshListener {
                getForecast(location)
            }

        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.extras?.get(BoxConstants.REFRESH_DATA_EXTRA) != null) {
            val location = BoxPreferences.getLocation(applicationContext)
            if (location != null) {
                title = location.name
                getForecast(location)
            }
        }
    }

    private fun getForecast(location: Location) {
        mainViewModel.getSuspendForecast(location.latitude, location.longitude)
            .observe(this, Observer { _resource ->
                when (_resource.status) {
                    Status.LOADING -> {
                        if (!swipe_refresh_layout.isRefreshing) {
                            message_text_view.visibility = View.VISIBLE
                            message_text_view.text = getString(R.string.loading)
                            currently_layout.visibility = View.GONE
                        }
                    }
                    Status.SUCCESS -> {

                        if (swipe_refresh_layout.isRefreshing) {
                            swipe_refresh_layout.isRefreshing = false
                        }

                        message_text_view.visibility = View.GONE
                        currently_layout.visibility = View.VISIBLE

                        val forecast = _resource.data as Forecast

                        currently_summary.text = forecast.currently.summary

                        currently_temperature.text =
                            BoxFormat.formatTemperature(forecast.currently.temperature)

                        currently_time.text =
                            BoxFormat.formatDateHour(forecast.currently.time, forecast.offset)

                        currently_precipitation.text = String.format(
                            getString(R.string.rain),
                            forecast.currently.precipProbability * 100
                        )

                        currently_humidity.text = String.format(
                            getString(R.string.humidity),
                            forecast.currently.humidity * 100
                        )

                        currently_wind_speed.text = String.format(
                            getString(R.string.wind_speed),
                            forecast.currently.windSpeed,
                            BoxFormat.getWindBearing(forecast.currently.windBearing)
                        )

                        daily_recycler_view.adapter =
                            ForecastRecyclerViewAdapter(
                                this,
                                forecast.daily.data,
                                forecast.timezone
                            )

                    }
                    Status.ERROR -> {
                        if (swipe_refresh_layout.isRefreshing) {
                            swipe_refresh_layout.isRefreshing = false
                        }
                        message_text_view.visibility = View.VISIBLE
                        message_text_view.text = _resource.message
                        currently_layout.visibility = View.GONE
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit_city) {
            val searchIntent = Intent(this, SearchLocationActivity::class.java)
            startActivity(searchIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        return super.onOptionsItemSelected(item)
    }

}
