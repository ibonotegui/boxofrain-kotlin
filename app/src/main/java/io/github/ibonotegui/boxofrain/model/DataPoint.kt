package io.github.ibonotegui.boxofrain.model

// in inches of liquid water per hour
// 0.002 very light
// 0.017 light precip
// 0.1 moderate precip
// 0.4 heavy precip
data class DataPoint(
    val time: Long,
    val summary: String,
    val icon: String,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val moonPhase: Double,
    val nearestStormDistance: Int,
    val nearestStormBearing: Int,
    val precipIntensity: Double,
    val precipIntensityMax: Double,
    val precipIntensityMaxTime: Long,
    val precipProbability: Double,
    val precipType: String,
    val precipAccumulation: Double,
    val temperature: Double,
    val temperatureMin: Double,
    val temperatureMinTime: Long,
    val temperatureMax: Double,
    val temperatureMaxTime: Long,
    val apparentTemperature: Double,
    val apparentTemperatureMin: Double,
    val apparentTemperatureMinTime: Long,
    val apparentTemperatureMax: Double,
    val apparentTemperatureMaxTime: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val humidity: Double,
    val pressure: Double,
    val visibility: Double,
    val ozone: Double
)
