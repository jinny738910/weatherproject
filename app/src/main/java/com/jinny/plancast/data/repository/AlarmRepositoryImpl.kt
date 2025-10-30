package com.jinny.plancast.data.repository


import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.domain.repository.AlarmRepository
import java.time.LocalDate

/**
 * AlarmRepository의 구현체
 * @param apiService Retrofit으로 생성된 API 서비스
 * @param apiKey 기상청 API 인증키
 */
class AlarmRepositoryImpl (
//    private val reminderScheduler: ReminderScheduler // AlarmManager, WorkManager 래퍼
) : AlarmRepository {

    override fun scheduleReminder(toDoEntity: ToDoEntity) {
        // 10분 전 알람 스케줄링
//        val triggerTime = todo.scheduledTime.minusMinutes(10)
//        reminderScheduler.schedule(triggerTime, todo, ReminderType.TEN_MINUTES_BEFORE)
    }

    override fun scheduleWeatherWarning(toDoEntity: ToDoEntity) {
        // 2시간 전 알람 스케줄링
//        val triggerTime = todo.scheduledTime.minusHours(2)
//        reminderScheduler.schedule(triggerTime, todo, ReminderType.TWO_HOURS_BEFORE_WARNING)
    }

    override fun scheduleLocationrWarning(toDoEntity: ToDoEntity) {
        // 할일 중 비슷한 위치인거 알림
//        val triggerTime = todo.scheduledTime.minusHours(2)
//        reminderScheduler.schedule(triggerTime, todo, ReminderType.TWO_HOURS_BEFORE_WARNING)
    }

    override fun scheduleDailyForecast(date: LocalDate) {
        // 전날 날씨 예보
//        val triggerTime = todo.scheduledTime.minusHours(2)
//        reminderScheduler.schedule(triggerTime, todo, ReminderType.TWO_HOURS_BEFORE_WARNING)
    }

}