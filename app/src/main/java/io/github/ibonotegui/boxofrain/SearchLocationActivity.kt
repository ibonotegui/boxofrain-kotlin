package io.github.ibonotegui.boxofrain

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.ibonotegui.boxofrain.util.BoxPreferences
import io.github.ibonotegui.boxofrain.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_search_location.*

class SearchLocationActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)
        //progress bar ui
        search_city_button.setOnClickListener {
            if (!text_input_edit_text_city.text.isNullOrEmpty()) {
                mainViewModel.searchCity(
                    applicationContext, text_input_edit_text_city.text.toString()
                ).observe(this, Observer { _resource ->
                    run {
                        if (_resource.data != null) {
                            BoxPreferences.setLocation(
                                applicationContext,
                                _resource.data.name,
                                _resource.data.latitude, _resource.data.longitude
                            )
                            val searchIntent = Intent(this, MainActivity::class.java)
                            startActivity(searchIntent)
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                _resource.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.enter_city),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
