package io.github.ibonotegui.boxofrain.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object BoxFormat {

    fun formatTemperature(temperature: Double): String {
        return temperature.roundToInt().toString() + "\u00B0"
    }

    fun formatDateDayLong(timeSeconds: Long, timezone : String) : String {
        val timeInMilliSeconds = timeSeconds * 1000
        val date = Date(timeInMilliSeconds)
        val simpleDateFormat = SimpleDateFormat("E\nd", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone(timezone)
        return simpleDateFormat.format(date)
    }

    fun formatDateHour(timeSeconds: Long, offsetHour: Double): String {
        val offsetInMilliSeconds: Double = offsetHour * 60 * 60 * 1000
        val timeInMilliSeconds = timeSeconds * 1000 + offsetInMilliSeconds.toLong()
        val date = Date(timeInMilliSeconds)
        val simpleDateFormat = SimpleDateFormat("E d h:mm a", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return simpleDateFormat.format(date)
    }

    fun getWindBearing(windBearingDegrees: Int): String {
        return if (windBearingDegrees > 320 || windBearingDegrees < 30) {
            "N"
        } else if (windBearingDegrees in 30..60) {
            "NE"
        } else if (windBearingDegrees in 61..119) {
            "E"
        } else if (windBearingDegrees in 120..150) {
            "SE"
        } else if (windBearingDegrees in 151..209) {
            "S"
        } else if (windBearingDegrees in 210..240) {
            "SW"
        } else if (windBearingDegrees in 241..289) {
            "W"
        } else if (windBearingDegrees in 290..320) {
            "NW"
        } else {
            "unknown"
        }
    }

}