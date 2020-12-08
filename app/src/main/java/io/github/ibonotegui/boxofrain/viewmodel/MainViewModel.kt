package io.github.ibonotegui.boxofrain.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import io.github.ibonotegui.boxofrain.data.BoxDatabase
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.model.Location
import io.github.ibonotegui.boxofrain.network.Resource
import io.github.ibonotegui.boxofrain.repository.BoxRepository
import io.github.ibonotegui.boxofrain.util.BoxConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// AndroidViewModel is the same as ViewModel, but it takes the application context as a parameter
// and makes it available as a property
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val boxRepository: BoxRepository

    val location: LiveData<io.github.ibonotegui.boxofrain.data.Location>

    init {
        //pass it instead? need a viewmodel factory
        val locationDao = BoxDatabase.getDatabase(application).locationDao()
        boxRepository = BoxRepository(locationDao)
        location = boxRepository.getLocation()
    }

    fun searchCity(context: Context, city: String): LiveData<Resource<Location>> {
        return boxRepository.searchCity(context, city)
    }

    fun getForecast(latitude: String, longitude: String): LiveData<Resource<Forecast>> {
        return boxRepository.getForecast(latitude, longitude)
    }

    fun getSuspendForecast(latitude: String, longitude: String): LiveData<Resource<Forecast>> =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            //kotlinx.coroutines.delay(4000)
            try {
                val forecast = boxRepository.getSuspendForecast(latitude, longitude)
                emit(Resource.success(forecast))
            } catch (ioException: Exception) {
                emit(Resource.error(null, ioException.message))
            }
        }

    // In Kotlin, all coroutines run inside a CoroutineScope.
    // A scope controls the lifetime of coroutines through its job.
    // When you cancel the job of a scope, it cancels all coroutines started in that scope.
    fun insertLocation(location: io.github.ibonotegui.boxofrain.data.Location) =
        viewModelScope.launch(Dispatchers.IO) {
            boxRepository.insertLocation(location)
        }

    fun updateLocation(location: io.github.ibonotegui.boxofrain.data.Location) =
        viewModelScope.launch(Dispatchers.IO) {
            boxRepository.updateLocation(location)
        }

    //no need for aa coroutine, room queries returning LiveData run on a separate thread
    fun getLocations(): LiveData<List<io.github.ibonotegui.boxofrain.data.Location>> {
        return boxRepository.getLocations()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(BoxConstants.TAG, "MainViewModel onCleared!")
    }

}
