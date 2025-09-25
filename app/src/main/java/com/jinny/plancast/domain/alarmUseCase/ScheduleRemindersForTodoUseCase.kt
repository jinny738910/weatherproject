package com.jinny.plancast.domain.alarmUseCase

import com.jinny.plancast.data.local.entity.ToDoEntity

import com.jinny.plancast.domain.repository.AlarmRepository

class ScheduleRemindersForTodoUseCase(private val repository: AlarmRepository) {
    operator fun invoke(toDoEntity: ToDoEntity) {
        // 기능 1: 10분 전 알림 예약
        repository.scheduleReminder(toDoEntity)
        // 기능 3: 2시간 전 날씨/위치 주의 알림 예약
        repository.scheduleWeatherWarning(toDoEntity)
    }
}
