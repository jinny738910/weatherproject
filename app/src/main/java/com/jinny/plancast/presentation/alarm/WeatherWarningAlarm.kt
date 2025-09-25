package com.jinny.plancast.presentation.alarm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

// 위치를 가져오고, 날씨 API를 호출하며, 최종적으로 알림을 생성하는 핵심 로직
class WeatherWarningWorker (
    private val context: Context, params: WorkerParameters,
//    private val checkWeatherAndLocationWarningUseCase: CheckWeatherAndLocationWarningUseCase, // UseCase 주입
    private val notificationHelper: NotificationHelper // 알림 생성 헬퍼
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val todoId = inputData.getInt("KEY_TODO_ID", -1)
        if (todoId == -1) return Result.failure()

        // Domain 계층의 UseCase를 실행하여 비즈니스 로직 처리
//        when (val result = checkWeatherAndLocationWarningUseCase(todoId)) {
//            is WarningResult.Success -> {
//                var message = ""
//                if (result.isRaining) message += "현재 비가 오고 있어요! ☔ "
//                if (result.isFarAway) message += "예정 장소와 멀리 떨어져 있어요! 🗺️"
//
//                if (message.isNotEmpty()) {
//                    notificationHelper.showNotification("할 일 주의 알림", message)
//                }
//            }
//            WarningResult.TodoNotFound -> return Result.failure()
//        }
        return Result.success()
    }
}