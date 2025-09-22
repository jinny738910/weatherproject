package com.jinny.plancast.presentation.weather

import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.domain.model.WeatherInfo

sealed class WeatherState {

    object UnInitialized: WeatherState()

    object Loading: WeatherState()

    data class Success(
        val weatherInfo: WeatherInfo
    ): WeatherState()

    object Delete: WeatherState()

    object Modify: WeatherState()

    object Error: WeatherState()

    object Write: WeatherState()

}
