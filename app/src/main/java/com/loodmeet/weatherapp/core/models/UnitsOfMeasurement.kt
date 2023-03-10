package com.loodmeet.weatherapp.core.models

import com.dmitLugg.weatherapp.R

data class UnitsOfMeasurementResIds(
    val temperatureUnitResId: Int,
    val windSpeedUnitResId: Int,
    val precipitationUnitResId: Int
) {
    companion object {
        object TemperatureUnits {
            const val CELSIUS = R.string.celsius
            const val FAHRENHEIT = R.string.fahrenheit
        }

        object WindSpeedUnits {
            const val METRES_PER_SECOND = R.string.metres_per_second
            const val KILOMETRES_PER_HOUR = R.string.kilometres_per_hour
            const val MILES_PER_HOUR = R.string.miles_per_hour
            const val KNOTS = R.string.knots
        }

        object PrecipitationUnits {
            const val MILLIMETER = R.string.millimeter
            const val INCH = R.string.inch
        }
    }
}