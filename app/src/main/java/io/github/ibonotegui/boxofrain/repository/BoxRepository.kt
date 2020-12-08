package io.github.ibonotegui.boxofrain.repository

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.ibonotegui.boxofrain.R
import io.github.ibonotegui.boxofrain.data.LocationDao
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.model.Location
import io.github.ibonotegui.boxofrain.network.BoxApi
import io.github.ibonotegui.boxofrain.network.Resource
import io.github.ibonotegui.boxofrain.util.BoxConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class BoxRepository(private val locationDao: LocationDao) {

    fun searchCity(context: Context, city: String): LiveData<Resource<Location>> {
        val liveData = MutableLiveData<Resource<Location>>()
        //TODO
        //liveData.value = Resource.loading(null)
        val geocoder = Geocoder(context)
        val executorService = Executors.newSingleThreadExecutor()
        executorService.submit {
            val addressList = geocoder.getFromLocationName(city, 10)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val location = Location(
                    address.locality,
                    address.latitude.toString(),
                    address.longitude.toString()
                )
                //return all addresses
                liveData.postValue(Resource.success(location))
            } else {
                liveData.postValue(Resource.error(null, context.getString(R.string.city_not_found)))
            }
        }
        return liveData
    }

    fun getForecast(latitude: String, longitude: String): LiveData<Resource<Forecast>> {
        val liveData = MutableLiveData<Resource<Forecast>>()
        liveData.value = Resource.loading(null)
        BoxApi.retrofitService.getForecast(latitude, longitude)
            .enqueue(object : Callback<Forecast> {
                override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                    Log.d(BoxConstants.TAG, "onResponse ${response.body()?.alert?.title}")
                    liveData.value = Resource.success(response.body())
                }

                override fun onFailure(call: Call<Forecast>, t: Throwable) {
                    Log.d(BoxConstants.TAG, "onFailure ${t.message}")
                    liveData.value = Resource.error(null, t.message)
                }
            })
        return liveData
    }

    suspend fun getSuspendForecast(latitude: String, longitude: String) =
        BoxApi.retrofitService.getSuspendForecast(latitude, longitude)

    // Room executes all queries on a separate thread?
    // Asynchronous queries—queries that return instances of LiveData or Flowable—
    // they asynchronously run the query on a background thread when needed.
    fun getLocation(): LiveData<io.github.ibonotegui.boxofrain.data.Location> =
        locationDao.getLocation()

    fun getLocations(): LiveData<List<io.github.ibonotegui.boxofrain.data.Location>> =
        locationDao.getLocations()

    //@WorkerThread
    suspend fun insertLocation(
        location: io.github.ibonotegui.boxofrain.data.Location
    ) = locationDao.insertLocation(location)

    suspend fun updateLocation(
        location: io.github.ibonotegui.boxofrain.data.Location
    ) = locationDao.updateLocation(location)

}
