package com.jinny.plancast.domain.repository

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.jinny.plancast.data.local.entity.ToDoEntity
import com.jinny.plancast.domain.model.PlacePrediction
import com.jinny.plancast.domain.model.WeatherInfo
import java.time.LocalDate

/**
 * 결제 서비스 관련 Repository 인터페이스
 */
interface BackendRepository {

    // API 호출 결과를 Result 타입으로 반환하여 성공/실패를 명확히 구분합니다.
    suspend fun fetchHelloMessage(): Result<String>
}