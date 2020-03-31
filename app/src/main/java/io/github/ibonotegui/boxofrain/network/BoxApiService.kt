package io.github.ibonotegui.boxofrain.network

import io.github.ibonotegui.boxofrain.BuildConfig
import io.github.ibonotegui.boxofrain.model.Forecast
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

//private const val BASE_URL = "https://api.darksky.net"

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .baseUrl(BASE_URL)
//    .build()

interface BoxApiService {
    @GET("/forecast/${BuildConfig.DARK_SKY_KEY}/{latitude},{longitude}")//?exclude=alerts,flags,minutely
    fun getForecast(
        @Path(value = "latitude") latitude: String,
        @Path(value = "longitude") longitude: String
    ): Call<Forecast>
}

object BoxApi {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://api.darksky.net")
        .build()
    val retrofitService: BoxApiService by lazy { retrofit.create(BoxApiService::class.java) }
}
