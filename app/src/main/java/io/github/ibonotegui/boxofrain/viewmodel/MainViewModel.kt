package io.github.ibonotegui.boxofrain.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.network.Resource
import io.github.ibonotegui.boxofrain.repository.BoxRepository

class MainViewModel : ViewModel() {

    private val boxRepository = BoxRepository()

//    init {
//        //boxRepository.getForecast()
//    }

    fun getForecast(latitude : String, longitude : String) : LiveData<Resource<Forecast>> {
        return boxRepository.getForecast(latitude, longitude)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("aaaa", "MainViewModel onCleared!")
    }

}
