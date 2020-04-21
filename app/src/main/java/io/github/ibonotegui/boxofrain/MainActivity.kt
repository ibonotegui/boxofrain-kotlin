package io.github.ibonotegui.boxofrain

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.ibonotegui.boxofrain.network.Status
import io.github.ibonotegui.boxofrain.util.BoxPreferences
import io.github.ibonotegui.boxofrain.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val location = BoxPreferences.getLocation(applicationContext)

        if (location == null) {
            val searchIntent = Intent(this, SearchLocationActivity::class.java)
            startActivity(searchIntent)
            finish()
        } else {

            title = location.name

            mainViewModel.getSuspendForecast(location.latitude, location.longitude)
                .observe(this, Observer { _resource ->
                    when (_resource.status) {
                        Status.LOADING -> {
                            timezone_text_view.text = getString(R.string.loading)
                        }
                        Status.SUCCESS -> {
                            //update ui
                            timezone_text_view.text = _resource.data?.timezone
                        }
                        Status.ERROR -> {
                            timezone_text_view.text = _resource.message
                        }
                    }
                })

            change_city_button.setOnClickListener {
                val searchIntent = Intent(this, SearchLocationActivity::class.java)
                startActivity(searchIntent)
                finish()
            }
        }
    }
}
