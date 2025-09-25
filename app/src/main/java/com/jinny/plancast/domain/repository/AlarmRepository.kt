package com.jinny.plancast.domain.repository

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.domain.model.PlacePrediction
import com.jinny.plancast.domain.model.WeatherInfo
import java.time.LocalDate

/**
 * 알림 스케줄링 관련 Repository 인터페이스
 */
interface AlarmRepository {

    // 알림 스케줄링 관련
    fun scheduleReminder(toDoEntity: ToDoEntity) // 10분 전 알림
    fun scheduleWeatherWarning(toDoEntity: ToDoEntity) // 2시간 전 날씨/위치 경고
    fun scheduleLocationrWarning(toDoEntity: ToDoEntity) // 할일 중 비슷한 위치인거 알림
    fun scheduleDailyForecast(date: LocalDate) // 전날 날씨 예보

}