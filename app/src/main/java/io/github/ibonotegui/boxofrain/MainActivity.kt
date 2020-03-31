package io.github.ibonotegui.boxofrain

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.ibonotegui.boxofrain.network.Status
import io.github.ibonotegui.boxofrain.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("aaaa", "sky ${BuildConfig.DARK_SKY_KEY}")
        //progress bar ui
        mainViewModel.getForecast("37.7749", "-122.4194")
            .observe(this, Observer { _resource ->
                //update ui
                Log.d("aaaa", "status ${_resource.status}")
                when (_resource.status) {
                    Status.LOADING -> {
                        timezone_text_view.text = "Loading..."
                        //Toast.makeText(applicationContext, "loading...", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        //update ui
                        timezone_text_view.text = _resource.data?.timezone
                        //Log.d("aaaa", "offset ${_resource.data?.offset}")
                    }
                    Status.ERROR -> {
                        timezone_text_view.text = _resource.message
                        //Toast.makeText(applicationContext, _resource.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
//        val handler = Handler()
//        handler.postDelayed({
//        }, 4000)
    }

}
