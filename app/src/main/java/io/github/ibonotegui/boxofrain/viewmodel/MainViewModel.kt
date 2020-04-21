package io.github.ibonotegui.boxofrain.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.model.Location
import io.github.ibonotegui.boxofrain.network.Resource
import io.github.ibonotegui.boxofrain.repository.BoxRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    private val boxRepository = BoxRepository()

    fun searchCity(context : Context, city : String) : LiveData<Resource<Location>> {
        return boxRepository.searchCity(context, city)
    }

    fun getForecast(latitude : String, longitude : String) : LiveData<Resource<Forecast>> {
        return boxRepository.getForecast(latitude, longitude)
    }

    fun getSuspendForecast(latitude : String, longitude : String): LiveData<Resource<Forecast>> = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        //kotlinx.coroutines.delay(4000)
        try {
            val forecast = boxRepository.getSuspendForecast(latitude, longitude)
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
