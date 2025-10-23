package com.jinny.plancast.domain.usecase.alarmUseCase


import com.jinny.plancast.domain.model.WarningResult
import com.jinny.plancast.domain.repository.AlarmRepository

class CheckWeatherAndLocationWarningUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(id: Long): WarningResult {
//        val todo = repository.getTodoById(todoId) ?: return WarningResult.TodoNotFound
//
//        val weather = repository.getWeatherForecast(todo.location.latitude, todo.location.longitude)
//        val currentLocation = repository.getCurrentLocation()
//
//        val distance = calculateDistance(currentLocation, todo.location)
//
        val isRaining = true
//            weather.condition == "Rain"
        val isFarAway = true
//            distance > 20.0 // 예: 20km 이상 떨어져 있을 때

        return WarningResult.Success(isRaining, isFarAway)
    }
}
