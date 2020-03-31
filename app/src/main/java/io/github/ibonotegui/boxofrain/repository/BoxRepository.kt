package io.github.ibonotegui.boxofrain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.ibonotegui.boxofrain.model.Forecast
import io.github.ibonotegui.boxofrain.network.BoxApi
import io.github.ibonotegui.boxofrain.network.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoxRepository {

    fun getForecast(latitude: String, longitude: String): LiveData<Resource<Forecast>> {
        val liveData = MutableLiveData<Resource<Forecast>>()
        liveData.value = Resource.loading(null)
        BoxApi.retrofitService.getForecast(latitude, longitude).enqueue(object : Callback<Forecast> {
            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                Log.d("aaaa", "onResponse ${response.body()?.alert?.title}")
                liveData.value = Resource.success(response.body())
            }
            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                Log.d("aaaa", "onFailure ${t.message}")
                liveData.value = Resource.error(null, t.message)
            }
        })
        return liveData
    }

}
