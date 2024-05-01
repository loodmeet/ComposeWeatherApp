package com.loodmeet.weatherapp.domain.mapper

import com.loodmeet.weatherapp.core.models.Location
import com.loodmeet.weatherapp.core.models.MeasurementUnitsSet
import com.loodmeet.weatherapp.core.utils.Temperature
import com.loodmeet.weatherapp.data.models.response.WeatherResponse
import com.loodmeet.weatherapp.di.AppScope
import com.loodmeet.weatherapp.ui.models.HourlyWeather
import com.loodmeet.weatherapp.ui.models.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import javax.inject.Inject

@AppScope
class WeatherResponseMapperImpl @Inject constructor() : WeatherResponseMapper<WeatherResponse> {

    private val dailyResponseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val hourlyResponseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    private val dailyFormatter = DateTimeFormatterBuilder()
        .appendPattern("EEE, d MMM")
        .parseDefaulting(ChronoField.YEAR, LocalDate.now().year.toLong())
        .toFormatter()
    private val hourlyFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override suspend fun map(
        response: WeatherResponse,
        measurementUnitsSet: MeasurementUnitsSet,
        location: Location
    ): List<Weather> =
        withContext(Dispatchers.Default) {
            return@withContext List(size = 7) { dailyIndex ->
                val daily = response.daily[dailyIndex]
                val translatedDailyWeather = TranslatedWeatherCode.fromWeatherCode(daily.weatherCode)
                Weather(
                    date = dailyFormatter.format(LocalDate.parse(daily.date, dailyResponseFormatter)),
                    translatedDailyWeather = translatedDailyWeather,
                    temperatureMax = Temperature(daily.temperatureMax).getValueAsString(),
                    temperatureMin = Temperature(daily.temperatureMin).getValueAsString(),
                    sunrise = hourlyFormatter.format(LocalDateTime.parse(daily.sunrise, hourlyResponseFormatter)),
                    sunset = hourlyFormatter.format(LocalDateTime.parse(daily.sunset, hourlyResponseFormatter)),
                    apparentTemperatureMin = Temperature(daily.apparentTemperatureMin).getValueAsString(),
                    apparentTemperatureMax = Temperature(daily.apparentTemperatureMax).getValueAsString(),
                    windSpeed = daily.windSpeed,
                    precipitationSum = daily.precipitationSum,
                    hourlyWeather = List(size = 8) { hourlyIndex ->
                        val hourly = daily.hourlyWeather[hourlyIndex]
                        val translatedHourlyWeather = TranslatedWeatherCode.fromWeatherCode(hourly.weatherCode)
                        HourlyWeather(
                            time = hourlyFormatter.format(LocalDateTime.parse(hourly.time, hourlyResponseFormatter)),
                            descriptionResId = translatedHourlyWeather.stringResId,
                            iconResId = if (hourly.isDay) translatedHourlyWeather.dayImageResId else translatedHourlyWeather.nightImageResId,
                            temperature = Temperature(hourly.temperature).getValueAsString()
                        )
                    },
                    measurementUnitsSet = measurementUnitsSet,
                    dayLengthIndicator = daily.dayLengthIndicator,
                )
            }
        }
}