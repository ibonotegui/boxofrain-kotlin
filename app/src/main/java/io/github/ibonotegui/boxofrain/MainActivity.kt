package io.github.ibonotegui.boxofrain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.ibonotegui.boxofrain.adapter.ForecastRecyclerViewAdapter
import io.github.ibonotegui.boxofrain.databinding.ActivityMainBinding
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.model.Location
import io.github.ibonotegui.boxofrain.network.Status
import io.github.ibonotegui.boxofrain.util.BoxConstants
import io.github.ibonotegui.boxofrain.util.BoxFormat
import io.github.ibonotegui.boxofrain.util.BoxPreferences
import io.github.ibonotegui.boxofrain.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var messageTextView: TextView
    private lateinit var currentlyHumidity: TextView
    private lateinit var currentlyPrecipitation: TextView
    private lateinit var currentlySummary: TextView
    private lateinit var currentlyTemperature: TextView
    private lateinit var currentlyTime: TextView
    private lateinit var currentlyWindSpeed: TextView
    private lateinit var currentlyLayout: LinearLayout
    private lateinit var dailyRecyclerView: RecyclerView

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO
        val location = BoxPreferences.getLocation(applicationContext)

        mainViewModel.getLocations().observe(this, Observer {
            Log.d("aaaa", "locations size: ${it.size}")
        })

        mainViewModel.location.observe(this, Observer { _location ->

            Log.d("aaaa", "location: $_location")

            if (_location == null) {

                val searchIntent = Intent(this, SearchLocationActivity::class.java)
                startActivity(searchIntent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()

            } else {

            }

        })

        if (location == null) {

            val searchIntent = Intent(this, SearchLocationActivity::class.java)
            startActivity(searchIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()

        } else {

            val binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            swipeRefreshLayout = binding.swipeRefreshLayout

            messageTextView = binding.messageTextView
            currentlyHumidity = binding.currentlyHumidity
            currentlyPrecipitation = binding.currentlyPrecipitation
            currentlySummary = binding.currentlySummary
            currentlyTemperature = binding.currentlyTemperature
            currentlyTime = binding.currentlyTime
            currentlyWindSpeed = binding.currentlyWindSpeed
            currentlyLayout = binding.currentlyLayout
            dailyRecyclerView = binding.dailyRecyclerView

            title = location.name

            dailyRecyclerView.layoutManager = LinearLayoutManager(this)
            dailyRecyclerView.setHasFixedSize(true)

            getForecast(location)

            swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.blue
                )
            )
            swipeRefreshLayout.setOnRefreshListener {
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
                        if (!swipeRefreshLayout.isRefreshing) {
                            messageTextView.visibility = View.VISIBLE
                            messageTextView.text = getString(R.string.loading)
                            currentlyLayout.visibility = View.GONE
                        }
                    }
                    Status.SUCCESS -> {

                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }

                        messageTextView.visibility = View.GONE
                        currentlyLayout.visibility = View.VISIBLE

                        val forecast = _resource.data as Forecast

                        currentlySummary.text = forecast.currently.summary

                        currentlyTemperature.text =
                            BoxFormat.formatTemperature(forecast.currently.temperature)

                        currentlyTime.text =
                            BoxFormat.formatDateHour(forecast.currently.time, forecast.offset)

                        currentlyPrecipitation.text = String.format(
                            getString(R.string.rain),
                            forecast.currently.precipProbability * 100
                        )

                        currentlyHumidity.text = String.format(
                            getString(R.string.humidity),
                            forecast.currently.humidity * 100
                        )

                        currentlyWindSpeed.text = String.format(
                            getString(R.string.wind_speed),
                            forecast.currently.windSpeed,
                            BoxFormat.getWindBearing(forecast.currently.windBearing)
                        )

                        dailyRecyclerView.adapter =
                            ForecastRecyclerViewAdapter(
                                this,
                                forecast.daily.data,
                                forecast.timezone
                            )

                    }
                    Status.ERROR -> {
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                        }
                        messageTextView.visibility = View.VISIBLE
                        messageTextView.text = _resource.message
                        currentlyLayout.visibility = View.GONE
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
