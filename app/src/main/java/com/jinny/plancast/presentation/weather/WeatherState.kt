package com.jinny.plancast.presentation.weather

import com.jinny.plancast.data.local.entity.ToDoEntity

sealed class WeatherState {

    object UnInitialized: WeatherState()

    object Loading: WeatherState()

    data class Success(
        val toDoItem: ToDoEntity
    ): WeatherState()

    object Delete: WeatherState()

    object Modify: WeatherState()

    object Error: WeatherState()

    object Write: WeatherState()

}
