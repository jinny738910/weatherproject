package aop.fastcampus.part5.chapter01.presentation.weather

import aop.fastcampus.part5.chapter01.data.entity.ToDoEntity


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
