package com.loodmeet.weatherapp.ui.veiw_models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.loodmeet.weatherapp.core.models.Location
import com.loodmeet.weatherapp.core.models.MeasurementUnitsSet
import com.loodmeet.weatherapp.di.AppScope
import com.loodmeet.weatherapp.domain.use_case.*
import com.loodmeet.weatherapp.ui.models.Weather
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainScreenViewModel(
    private val fetchWeatherUseCase: FetchWeatherUseCase,
    private val fetchLocationUseCase: FetchLocationUseCase,
    private val fetchMeasurementUnitsSet: FetchMeasurementUnitsSet,
    private val saveLocationUseCase: SaveLocationUseCase,
    private val saveMeasurementUnitsSet: SaveMeasurementUnitsSet
) : ViewModel() {

    private val weatherData: MutableList<Weather> = mutableListOf()
    private val isLoading = mutableStateOf(true)
    private lateinit var location: Location
    private lateinit var measurementUnitsSet: MeasurementUnitsSet

    fun getWeatherData(): List<Weather> = weatherData
    fun getIsLoading(): State<Boolean> = isLoading
    fun getMeasurementUnitsSet(): MeasurementUnitsSet = measurementUnitsSet
    fun getLocation(): Location = location

    init {
        fetchWeather()
    }

    private fun fetchWeather() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                location = fetchLocationUseCase.execute()
                measurementUnitsSet = fetchMeasurementUnitsSet.execute()
                weatherData.clear()
                weatherData.addAll(fetchWeatherUseCase.execute())
                isLoading.value = false
            } catch (e: Exception) {
                isLoading.value = true
            }

        }
    }

    fun changeMeasurementUnitsSet(measurementUnitsSet: MeasurementUnitsSet) {
        viewModelScope.launch {
            saveMeasurementUnitsSet.execute(measurementUnitsSet = measurementUnitsSet)
            fetchWeather()
        }
    }

    fun changeLocation(location: Location) {
        viewModelScope.launch {
            saveLocationUseCase.execute(location = location)
            fetchWeather()
        }
    }

    fun fetchLocationList(): List<Location> {
        return Location.getList()
    }

    @AppScope
    class Factory @Inject constructor(
        private val fetchWeatherUseCase: FetchWeatherUseCase,
        private val fetchLocationUseCase: FetchLocationUseCase,
        private val fetchMeasurementUnitsSet: FetchMeasurementUnitsSet,
        private val saveLocationUseCase: SaveLocationUseCase,
        private val saveMeasurementUnitsSet: SaveMeasurementUnitsSet
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainScreenViewModel(
                fetchWeatherUseCase = fetchWeatherUseCase,
                fetchLocationUseCase = fetchLocationUseCase,
                fetchMeasurementUnitsSet = fetchMeasurementUnitsSet,
                saveLocationUseCase = saveLocationUseCase,
                saveMeasurementUnitsSet = saveMeasurementUnitsSet
            ) as T
        }
    }
}