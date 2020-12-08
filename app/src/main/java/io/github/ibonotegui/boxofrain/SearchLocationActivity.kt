package io.github.ibonotegui.boxofrain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.ibonotegui.boxofrain.adapter.LocationRecyclerViewAdapter
import io.github.ibonotegui.boxofrain.data.Location
import io.github.ibonotegui.boxofrain.databinding.ActivitySearchLocationBinding
import io.github.ibonotegui.boxofrain.util.BoxConstants
import io.github.ibonotegui.boxofrain.util.BoxPreferences
import io.github.ibonotegui.boxofrain.viewmodel.MainViewModel

class SearchLocationActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locationRecyclerView = binding.searchRecyclerView
        locationRecyclerView.layoutManager = LinearLayoutManager(this)
        locationRecyclerView.setHasFixedSize(true)

        val locationRecyclerViewAdapter = LocationRecyclerViewAdapter(this,
            LocationRecyclerViewAdapter.OnLocationClickListener { _location ->
                //handle logic in view model
                _location.isDefault = true
                mainViewModel.updateLocation(_location)
                Log.d("aaaa", _location.name)
                Log.d("aaaa", _location.latitude)
                Log.d("aaaa", _location.longitude)
            })
        locationRecyclerView.adapter = locationRecyclerViewAdapter

        mainViewModel.getLocations().observe(this, Observer { locations ->
            locations?.let {
                //set adapter data instead and only update changed
                locationRecyclerViewAdapter.setData(locations)

//                locationRecyclerView.adapter = LocationRecyclerViewAdapter(this,
//                    it,
//                    LocationRecyclerViewAdapter.OnLocationClickListener { _location ->
//                        //handle logic in view model
//                        _location.is_default = false
//                        //mainViewModel.updateLocation(_location)
//                        Log.d("aaaa", _location.name)
//                        Log.d("aaaa", _location.latitude)
//                        Log.d("aaaa", _location.longitude)
//                    })
            }
        })

        if (BoxPreferences.getLocation(applicationContext) != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.edit_city)
        }

        //progress bar ui
        binding.searchCityButton.setOnClickListener {
            if (!binding.textInputEditTextCity.text.isNullOrEmpty()) {
                mainViewModel.searchCity(
                    applicationContext, binding.textInputEditTextCity.text.toString()
                ).observe(this, Observer { resource ->
                    run {
                        if (resource.data != null) {
                            val locationNames = arrayOfNulls<String>(1)
                            //for ((index, name) in discoveredPeripheralList.keys.withIndex()) {
                            locationNames[0] = resource.data.name
                            //}
                            val builder = AlertDialog.Builder(this, R.style.BoxDialogTheme)
                            builder.setTitle(getString(R.string.select_city))
                            builder.setNegativeButton(R.string.cancel, null)
                            builder.setItems(locationNames) { _, which ->
                                // insert data and update previous default
                                // display a confirmation dialog if multiple locations
                                val location = Location(
                                    resource.data.name,
                                    resource.data.latitude,
                                    resource.data.longitude,
                                    true
                                )
                                mainViewModel.insertLocation(location)

                                BoxPreferences.setLocation(
                                    applicationContext,
                                    resource.data.name,
                                    resource.data.latitude, resource.data.longitude
                                )

                                val mainIntent = Intent(this, MainActivity::class.java)
                                mainIntent.putExtra(BoxConstants.REFRESH_DATA_EXTRA, true)
                                mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(mainIntent)
                                overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                                finish()
                            }
                            val dialog = builder.create()
                            dialog.show()
                        } else {
                            Toast.makeText(applicationContext, resource.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
            } else {
                Toast.makeText(
                    applicationContext, getString(R.string.enter_city), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(mainIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
