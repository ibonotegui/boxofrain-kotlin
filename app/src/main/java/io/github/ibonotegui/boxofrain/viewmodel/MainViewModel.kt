package io.github.ibonotegui.boxofrain.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.network.Resource
import io.github.ibonotegui.boxofrain.repository.BoxRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    private val boxRepository = BoxRepository()

//    init {
//        //boxRepository.getForecast()
//    }

    fun getForecast(latitude : String, longitude : String) : LiveData<Resource<Forecast>> {
        return boxRepository.getForecast(latitude, longitude)
    }

    val getSuspendForecast: LiveData<Resource<Forecast>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val forecast = boxRepository.getSuspendForecast("37.7749", "-122.4194")
            emit(Resource.success(forecast))
        } catch(ioException: Exception) {
            emit(Resource.error(null, ioException.message))
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("aaaa", "MainViewModel onCleared!")
    }

}
