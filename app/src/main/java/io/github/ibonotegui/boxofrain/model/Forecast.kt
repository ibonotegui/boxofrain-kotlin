package io.github.ibonotegui.boxofrain.model

data class Forecast(
    val timezone: String,
    val latitude: Double,
    val longitude: Double,
    val offset: Double,
    val currently: DataPoint,
    val daily: DataBlock,
    val hourly: DataBlock,
    val minutely: DataBlock,
    val flags: Flags,
    val alert: Alert
)
